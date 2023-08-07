package com.example.mvpmongo.controller;

import com.example.mvpmongo.model.ClientData;
import com.example.mvpmongo.model.dto.other.SignedDocument;
import com.example.mvpmongo.model.dto.request.SignApplicationFormRequest;
import com.example.mvpmongo.model.dto.request.SignPersonalDataProcessingRequest;
import com.example.mvpmongo.model.dto.response.DocumentResponse;
import com.example.mvpmongo.service.ClientDataService;
import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/document-service")
public class ClientDataController {

    private final ClientDataService clientDataService;

    @PostMapping("/sign-application-from")
    public ClientData addSignedApplicationFromToClientList(@RequestBody SignApplicationFormRequest form,
                                                           @RequestParam String clientId,
                                                           @RequestParam String documentId) {
        return clientDataService.signApplicationFrom(form, clientId, documentId);
    }

    @PostMapping("/sign-personal")
    public ClientData addSignedPersonalDataFromToClientList(@RequestBody SignPersonalDataProcessingRequest form,
                                                            @RequestParam String clientId,
                                                            @RequestParam String documentId) {
        return clientDataService.signPersonalDataForm(form, clientId, documentId);
    }

    @PostMapping("/sign-nodata")
    public ClientData addSignedDocumentWithoutDataToClientList(@RequestParam String clientId,
                                                               @RequestParam String documentId) {
        return clientDataService.signNoDataFrom(clientId, documentId);
    }


    @GetMapping("/application-form-pdf")
    public ResponseEntity<Resource> getApplicationFormAsPdf(@RequestParam String documentId,
                                                           @RequestParam String clientId
    ) throws IOException, Docx4JException {
        DocumentResponse documentAsPdf = clientDataService.getApplicationFormAsPdf(documentId, clientId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                        documentAsPdf.filename() + "\"").contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(documentAsPdf.file());
    }

    @GetMapping("/personal-form-pdf")
    public ResponseEntity<Resource> getPersonalDataFormAsPdf(@RequestParam String documentId,
                                                            @RequestParam String clientId
    ) throws IOException, Docx4JException {
        DocumentResponse documentAsPdf = clientDataService.getPersonalDataFromAsPdf(documentId, clientId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                        documentAsPdf.filename() + "\"").contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(documentAsPdf.file());
    }

    @GetMapping("/search-documentId")

    public ResponseEntity<List<SignedDocument>> getListOfSignedDocumentsByDocumentId(@RequestParam String documentId,
                                                                                     @RequestParam String clientId) {
        return ResponseEntity.ok(clientDataService.searchSignedDocumentByDocumentId(clientId, documentId));
    }

    @GetMapping("/search-package")
    public ResponseEntity<List<SignedDocument>> getListOfSignedDocumentsByDocumentPackage(@RequestParam String documentPackage,
                                                                                          @RequestParam String clientId) {
        return ResponseEntity.ok(clientDataService.searchSignedDocumentByDocumentPackage(clientId, documentPackage));
    }
}
