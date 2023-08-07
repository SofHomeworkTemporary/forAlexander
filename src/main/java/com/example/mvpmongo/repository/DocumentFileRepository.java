package com.example.mvpmongo.repository;

import com.example.mvpmongo.model.DocumentFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentFileRepository extends MongoRepository<DocumentFile, String> {
}
