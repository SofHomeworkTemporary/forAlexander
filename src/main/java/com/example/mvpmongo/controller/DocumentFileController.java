package com.example.mvpmongo.controller;

import com.example.mvpmongo.model.dto.response.DocumentResponse;
import com.example.mvpmongo.service.DocumentFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/document-service")
@RequiredArgsConstructor
public class DocumentFileController {

    private final DocumentFileService documentService;


    @PostMapping(value = "/document",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addDocument(@ModelAttribute MultipartFile document,
                              @RequestParam Boolean hasFields,
                              @RequestParam String documentPackage,
                              @RequestParam Boolean signed) throws IOException {
        return documentService.addDocument(document, hasFields, documentPackage, signed);
    }

    @GetMapping("/document/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable String id) {
        DocumentResponse documentResponse = documentService.getDocument(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + documentResponse.filename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(documentResponse.file());
    }

}
