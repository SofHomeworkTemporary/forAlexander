package com.example.mvpmongo.service;

import com.example.mvpmongo.model.DocumentFile;
import com.example.mvpmongo.model.dto.response.DocumentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocumentFileService {


    DocumentResponse getDocument(String id);
    String addDocument(MultipartFile document, Boolean hasFields, String documentPackage, Boolean signed) throws IOException;
}
