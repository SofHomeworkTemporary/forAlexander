package com.example.mvpmongo.service.impl;

import com.example.mvpmongo.exception.MongoException;
import com.example.mvpmongo.model.DocumentFile;
import com.example.mvpmongo.model.dto.response.DocumentResponse;
import com.example.mvpmongo.repository.DocumentFileRepository;
import com.example.mvpmongo.service.DocumentFileService;
import lombok.RequiredArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DocumentFileServiceImpl implements DocumentFileService {

    private final DocumentFileRepository documentFileRepository;


    @Override
    public String addDocument(MultipartFile document, Boolean hasFields, String documentPackage, Boolean signed) throws IOException {
        DocumentFile documentFile = new DocumentFile();
        documentFile.setTitle(document.getOriginalFilename());
        documentFile.setSigned(signed);
        documentFile.setHasDataFields(hasFields);
        documentFile.setDocumentPackage(documentPackage);
        documentFile.setDocument(new Binary(BsonBinarySubType.BINARY, document.getBytes()));
        return documentFileRepository.save(documentFile).getId();
    }

    @Override
    public DocumentResponse getDocument(String id) {
        DocumentFile documentFile = documentFileRepository.findById(id)
                .orElseThrow(() -> new MongoException("No document found"));
        Resource resource = new ByteArrayResource(documentFile.getDocument().getData());

        return new DocumentResponse(resource,documentFile.getTitle());
    }

}
