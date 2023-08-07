package com.example.mvpmongo.model;

import com.example.mvpmongo.model.dto.other.SignedDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@Document(collection = "client-data")
public class ClientData {
    private String id;
    private String name;
    private String clientId;
    private LocalDateTime dateBirth;
    private String addressReg;
    private String addressFact;
    private String passportSeries;
    private String passportNumber;
    private String mobilePhone;
    private String anotherMobilePhone;
    private String email;
    private String oneMoreEmail;
    private String cardName;
    private String currencyName;
    private String paymentSystem;
    private String tariff;
    private LocalDateTime signingDate;
    private LocalDateTime passportDateIssued;
    private String passportIssuerCode;
    private String passportIssuingOffice;
    private List<SignedDocument> documents;
}
