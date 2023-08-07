package com.example.mvpmongo.model.dto.other;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignedDocument {
    @JsonProperty("clientId")
    private String clientId;
    @JsonProperty("documentId")
    private String documentId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("signed")
    private Boolean signed = false;
    @JsonProperty("documentPackage")
    private String documentPackage;
}
