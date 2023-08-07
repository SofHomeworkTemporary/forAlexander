package com.example.mvpmongo.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SignApplicationFormRequest(@JsonProperty("name") String name,
                                         @JsonProperty("dateBirth") LocalDateTime dateBirth,
                                         @JsonProperty("addressReg") String addressReg,
                                         @JsonProperty("addressFact") String addressFact,
                                         @JsonProperty("passportSeries") String passportSeries,
                                         @JsonProperty("passportNumber") String passportNumber,
                                         @JsonProperty("mobilePhone") String mobilePhone,
                                         @JsonProperty("anotherMobilePhone") String anotherMobilePhone,
                                         @JsonProperty("email") String email,
                                         @JsonProperty("oneMoreEmail") String oneMoreEmail,
                                         @JsonProperty("cardName") String cardName,
                                         @JsonProperty("currencyName") String currencyName,
                                         @JsonProperty("paymentSystem") String paymentSystem,
                                         @JsonProperty("tariff") String tariff,
                                         @JsonProperty("signingDate") LocalDateTime signingDate) {

}
