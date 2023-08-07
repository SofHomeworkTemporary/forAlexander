package com.example.mvpmongo.repository;

import com.example.mvpmongo.model.ClientData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientDataRepository extends MongoRepository<ClientData, String> {

    Optional<ClientData> findByClientId(String clientId);
}
