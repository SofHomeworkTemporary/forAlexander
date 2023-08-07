package com.example.mvpmongo.mapper;

import com.example.mvpmongo.model.ClientData;
import com.example.mvpmongo.model.dto.request.SignApplicationFormRequest;
import com.example.mvpmongo.model.dto.request.SignPersonalDataProcessingRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientDataMapper {

    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "dateBirth", source = "request.dateBirth")
    @Mapping(target = "addressReg", source = "request.addressReg")
    @Mapping(target = "addressFact", source = "request.addressFact")
    @Mapping(target = "passportSeries", source = "request.passportSeries")
    @Mapping(target = "passportNumber", source = "request.passportNumber")
    @Mapping(target = "mobilePhone", source = "request.mobilePhone")
    @Mapping(target = "anotherMobilePhone", source = "request.anotherMobilePhone")
    @Mapping(target = "email", source = "request.email")
    @Mapping(target = "oneMoreEmail", source = "request.oneMoreEmail")
    @Mapping(target = "cardName", source = "request.cardName")
    @Mapping(target = "currencyName", source = "request.currencyName")
    @Mapping(target = "paymentSystem", source = "request.paymentSystem")
    @Mapping(target = "tariff", source = "request.tariff")
    @Mapping(target = "signingDate", source = "request.signingDate")
    ClientData getClientDataFromSignApplicationFormRequest(SignApplicationFormRequest request);

    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "passportDateIssued", source = "request.passportDateIssued")
    @Mapping(target = "passportSeries", source = "request.passportSeries")
    @Mapping(target = "passportNumber", source = "request.passportNumber")
    @Mapping(target = "passportIssuerCode", source = "request.issuerCode")
    @Mapping(target = "passportIssuingOffice", source = "request.issuingOffice")
    @Mapping(target = "mobilePhone", source = "request.mobilePhone")
    @Mapping(target = "signingDate", source = "request.signingDate")
    ClientData getClientDataFromSignPersonalDataProcessingRequest(SignPersonalDataProcessingRequest request);

}
