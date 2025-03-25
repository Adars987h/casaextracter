package com.simplifymoney.casaextracter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class AccountDetailsDTO {
    // Getters and Setters
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Opening Balance")
    private String openingBalance;

    @JsonProperty("Closing Balance")
    private String closingBalance;
}

