package com.example.mvpmongo.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SignPersonalDataProcessingRequest(@JsonProperty("name") String name,
                                                @JsonProperty("passportDateIssued") LocalDateTime passportDateIssued,
                                                @JsonProperty("passportSeries") String passportSeries,
                                                @JsonProperty("passportNumber") String passportNumber,
                                                @JsonProperty("issuerCode") String issuerCode,
                                                @JsonProperty("issuingOffice") String issuingOffice,
                                                @JsonProperty("mobilePhone") String mobilePhone,
                                                @JsonProperty("signingDate") LocalDateTime signingDate) {


}
