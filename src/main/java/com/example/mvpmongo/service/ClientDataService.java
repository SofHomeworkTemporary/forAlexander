package com.example.mvpmongo.service;

import com.example.mvpmongo.model.ClientData;
import com.example.mvpmongo.model.dto.other.SignedDocument;
import com.example.mvpmongo.model.dto.request.SignPersonalDataProcessingRequest;
import com.example.mvpmongo.model.dto.response.DocumentResponse;
import com.example.mvpmongo.model.dto.request.SignApplicationFormRequest;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import java.io.IOException;
import java.util.List;

public interface ClientDataService {
    ClientData signApplicationFrom(SignApplicationFormRequest form, String clientId, String documentId);
    ClientData signNoDataFrom(String clientId, String documentId);
    ClientData signPersonalDataForm(SignPersonalDataProcessingRequest form, String clientId, String documentId);

    List<SignedDocument> searchSignedDocumentByDocumentId(String requestedClientId, String requestedDocumentId);
    List<SignedDocument> searchSignedDocumentByDocumentPackage(String requestedClientId, String documentPackage);

    DocumentResponse getApplicationFormAsPdf(String documentId, String clientId) throws Docx4JException, IOException;

    DocumentResponse getPersonalDataFromAsPdf(String documentId, String clientId) throws IOException, Docx4JException;


}
