package com.example.mvpmongo.service.impl;

import com.example.mvpmongo.mapper.ClientDataMapper;
import com.example.mvpmongo.model.ClientData;
import com.example.mvpmongo.model.DocumentFile;
import com.example.mvpmongo.model.dto.other.SignedDocument;
import com.example.mvpmongo.model.dto.other.SignedDocumentUnwind;
import com.example.mvpmongo.model.dto.request.SignApplicationFormRequest;
import com.example.mvpmongo.model.dto.request.SignPersonalDataProcessingRequest;
import com.example.mvpmongo.model.dto.response.DocumentResponse;
import com.example.mvpmongo.repository.ClientDataRepository;
import com.example.mvpmongo.repository.DocumentFileRepository;
import com.example.mvpmongo.service.ClientDataService;
import de.phip1611.Docx4JSRUtil;
import lombok.RequiredArgsConstructor;
import org.bson.types.Binary;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientDataServiceImpl implements ClientDataService {
    private final ClientDataRepository clientDataRepository;
    private final DocumentFileRepository documentFileRepository;
    private final MongoTemplate mongoTemplate;
    private final ClientDataMapper clientDataMapper;

    @Override
    public ClientData signNoDataFrom(String clientId, String documentId) {
        DocumentFile document = documentFileRepository.findById(documentId).orElseThrow(
                () -> new RuntimeException("Не тот документ"));
        Optional<ClientData> clientData = clientDataRepository.findByClientId(clientId);
        ClientData data = null;
        if (clientData.isEmpty()) {
            data = ClientData.builder()
                    .clientId(clientId)
                    .documents(new ArrayList<>())
                    .build();
            List<SignedDocument> documents = getSignedDocuments(clientId, documentId, document, data);
            data.setDocuments(documents);
            return clientDataRepository.save(data);
        } else {
            ClientData existingClient = clientData.get();
            List<SignedDocument> documents = getSignedDocuments(clientId, documentId, document, existingClient);
            existingClient.setDocuments(documents);
            return clientDataRepository.save(existingClient);
        }
    }

    @Override
    public ClientData signApplicationFrom(SignApplicationFormRequest form, String clientId, String documentId) {
        DocumentFile document = documentFileRepository.findById(documentId).orElseThrow(
                () -> new RuntimeException("Не тот документ"));
        Optional<ClientData> clientData = clientDataRepository.findByClientId(clientId);
        ClientData data = null;
        if (clientData.isEmpty()) {
            data = clientDataMapper.getClientDataFromSignApplicationFormRequest(form);
            data.setClientId(clientId);
            data.setDocuments(new ArrayList<>());
            List<SignedDocument> documents = getSignedDocuments(clientId, documentId, document, data);
            data.setDocuments(documents);
            return clientDataRepository.save(data);
        } else {
            ClientData existingClient = clientData.get();
            existingClient.setName(form.name())
                    .setClientId(clientId)
                    .setDateBirth(form.dateBirth())
                    .setAddressReg(form.addressReg())
                    .setAddressFact(form.addressFact())
                    .setPassportSeries(form.passportSeries())
                    .setPassportNumber(form.passportNumber())
                    .setMobilePhone(form.mobilePhone())
                    .setAnotherMobilePhone(form.anotherMobilePhone())
                    .setEmail(form.email())
                    .setOneMoreEmail(form.oneMoreEmail())
                    .setCardName(form.cardName())
                    .setCurrencyName(form.currencyName())
                    .setPaymentSystem(form.paymentSystem())
                    .setTariff(form.tariff())
                    .setSigningDate(form.signingDate());
            List<SignedDocument> documents = getSignedDocuments(clientId, documentId, document, existingClient);
            existingClient.setDocuments(documents);
            return clientDataRepository.save(existingClient);
        }

    }

    @Override
    public ClientData signPersonalDataForm(SignPersonalDataProcessingRequest form, String clientId, String documentId) {
        DocumentFile document = documentFileRepository.findById(documentId).orElseThrow(
                () -> new RuntimeException("Не тот документ"));
        Optional<ClientData> clientData = clientDataRepository.findByClientId(clientId);
        ClientData data = null;
        if (clientData.isEmpty()) {
            data = clientDataMapper.getClientDataFromSignPersonalDataProcessingRequest(form);
            data.setClientId(clientId);
            data.setDocuments(new ArrayList<>());
            List<SignedDocument> documents = getSignedDocuments(clientId, documentId, document, data);
            data.setDocuments(documents);
            return clientDataRepository.save(data);
        } else {
            ClientData existingClient = clientData.get();
            existingClient.setName(form.name())
                    .setClientId(clientId)
                    .setPassportDateIssued(form.passportDateIssued())
                    .setPassportSeries(form.passportSeries())
                    .setPassportNumber(form.passportNumber())
                    .setPassportIssuerCode(form.issuerCode())
                    .setPassportIssuingOffice(form.issuingOffice())
                    .setMobilePhone(form.mobilePhone())
                    .setSigningDate(form.signingDate());
            List<SignedDocument> documents = getSignedDocuments(clientId, documentId, document, existingClient);
            existingClient.setDocuments(documents);
            return clientDataRepository.save(existingClient);
        }
    }

    @Override
    public List<SignedDocument> searchSignedDocumentByDocumentId(String requestedClientId, String requestedDocumentId) {
        UnwindOperation unwind = Aggregation.unwind("documents");
        MatchOperation match = Aggregation.match(Criteria.where("documents.clientId").is(requestedClientId).and("documents.documentId").is(requestedDocumentId));
        Aggregation aggregation = Aggregation.newAggregation(unwind, match);
        AggregationResults<SignedDocumentUnwind> results = mongoTemplate.aggregate(aggregation, "client-data",
                SignedDocumentUnwind.class);
        return results.getMappedResults().stream().map(SignedDocumentUnwind::documents).toList();
    }

    @Override
    public List<SignedDocument> searchSignedDocumentByDocumentPackage(String requestedClientId, String documentPackage) {
        UnwindOperation unwind = Aggregation.unwind("documents");
        MatchOperation match = Aggregation.match(Criteria.where("documents.clientId")
                .is(requestedClientId)
                .and("documents.documentPackage").is(documentPackage));
        Aggregation aggregation = Aggregation.newAggregation(unwind, match);
        AggregationResults<SignedDocumentUnwind> results =
                mongoTemplate.aggregate(aggregation, "client-data",
                        SignedDocumentUnwind.class);
        return results.getMappedResults().stream().map(SignedDocumentUnwind::documents).toList();
    }

    @Override
    public DocumentResponse getApplicationFormAsPdf(String documentId, String clientId) throws Docx4JException, IOException {
        ClientData clientData = performChecks(documentId, clientId);
        DocumentFile documentFile = documentFileRepository
                .findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document template with this id is not present"));
        Binary document = documentFile.getDocument();
        byte[] fileBytes = document.getData();
        InputStream targetStream = new ByteArrayInputStream(fileBytes);
        WordprocessingMLPackage mlPackage = WordprocessingMLPackage.load(targetStream);
        Map<String, String> replacementMap = getReplacementMapForApplicationForm(clientData);
        return getDocumentResponse(documentFile, mlPackage, replacementMap);
    }

    @Override
    public DocumentResponse getPersonalDataFromAsPdf(String documentId, String clientId) throws IOException, Docx4JException {
        ClientData clientData = performChecks(documentId, clientId);
        DocumentFile documentFile = documentFileRepository
                .findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document template with this id is not present"));
        Binary document = documentFile.getDocument();
        byte[] fileBytes = document.getData();
        InputStream targetStream = new ByteArrayInputStream(fileBytes);
        WordprocessingMLPackage mlPackage = WordprocessingMLPackage.load(targetStream);
        Map<String, String> replacementMap = getReplacementMapForPersonalDataProcessing(clientData);
        return getDocumentResponse(documentFile, mlPackage, replacementMap);
    }

    private DocumentResponse getDocumentResponse(DocumentFile documentFile, WordprocessingMLPackage mlPackage, Map<String, String> replacementMap) throws Docx4JException, IOException {
        Docx4JSRUtil.searchAndReplace(mlPackage, replacementMap);
        String outputFilePath = UUID.randomUUID() + ".pdf";
        FileOutputStream os = new FileOutputStream(outputFilePath);
        Docx4J.toPDF(mlPackage, os);
        os.flush();
        os.close();
        byte[] pdf = Files.readAllBytes(Path.of(outputFilePath));
        Resource resource = new ByteArrayResource(pdf);
        Files.delete(Path.of(outputFilePath));
        return new DocumentResponse(resource, documentFile.getTitle().replace(".docx", ".pdf"));
    }

    private ClientData performChecks(String documentId, String clientId) {
        ClientData clientData = clientDataRepository
                .findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Client is not present in db"));
        clientData
                .getDocuments()
                .stream()
                .filter(document -> document.getDocumentId().equals(documentId))
                .findFirst().orElseThrow(() -> new RuntimeException("Document with this id is not present"));
        return clientData;
    }

    private static Map<String, String> getReplacementMapForApplicationForm(ClientData clientData) {
        Map<String, String> replacementMap = new HashMap<>();
        replacementMap.put("{NAME}", clientData.getName());
        replacementMap.put("{DATE_OF_BIRTH}", clientData.getDateBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        replacementMap.put("{ADDRESS_REGISTRATION}", clientData.getAddressReg());
        replacementMap.put("{ADDRESS}", clientData.getAddressFact());
        replacementMap.put("{PASSPORT_SERIAL_NUMBER}", clientData.getPassportSeries());
        replacementMap.put("{PASSPORT_NUMBER}", clientData.getPassportNumber());
        replacementMap.put("{MOBILE_PHONE}", clientData.getMobilePhone());
        replacementMap.put("{ONE_MORE_MOBILE_PHONE}", clientData.getAnotherMobilePhone());
        replacementMap.put("{EMAIL}", clientData.getEmail());
        replacementMap.put("{ONE_MORE_EMAIL}", clientData.getOneMoreEmail());
        replacementMap.put("{CARD_NAME}", clientData.getCardName());
        replacementMap.put("{CURRENCY_NAME}", clientData.getCurrencyName());
        replacementMap.put("{PAYMENT_SYSTEM}", clientData.getPaymentSystem());
        replacementMap.put("{TARIFF}", clientData.getTariff());
        replacementMap.put("{DATE}", clientData.getSigningDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        return replacementMap;
    }

    private static Map<String, String> getReplacementMapForPersonalDataProcessing(ClientData clientData) {
        Map<String, String> replacementMap = new HashMap<>();
        replacementMap.put("{NAME}", clientData.getName());
        replacementMap.put("{PASSPORT_SERIES}", clientData.getPassportSeries());
        replacementMap.put("{PASSPORT_NUMBER}", clientData.getPassportNumber());
        replacementMap.put("{DATE_ISSUED}", clientData.getPassportDateIssued().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        replacementMap.put("{ISSUER_CODE}", clientData.getPassportIssuerCode());
        replacementMap.put("{ISSUING_OFFICE}", clientData.getPassportIssuingOffice());
        replacementMap.put("{MAIN_TELEPHONE_NUMBER}", clientData.getMobilePhone());
        replacementMap.put("{SIGNING_DATE}", clientData.getSigningDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        return replacementMap;
    }

    private static List<SignedDocument> getSignedDocuments(String clientId, String documentId, DocumentFile document, ClientData data) {
        SignedDocument signedDocument = SignedDocument.builder()
                .clientId(clientId)
                .documentId(documentId)
                .title(document.getTitle())
                .signed(true)
                .documentPackage(document.getDocumentPackage())
                .build();
        List<SignedDocument> documents = data.getDocuments();
        documents.add(signedDocument);
        return documents;
    }

}
