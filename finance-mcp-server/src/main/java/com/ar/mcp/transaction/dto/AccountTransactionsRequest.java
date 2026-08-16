package com.ar.mcp.transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

@Data
public class AccountTransactionsRequest {

    @JsonProperty(required = true)
    @JsonPropertyDescription(
            "Financial account number, for example ACC-1002"
    )
    private String accountNumber;

    @JsonProperty(required = false)
    @JsonPropertyDescription(
            "Start date in yyyy-MM-dd format"
    )
    private String fromDate;

    @JsonProperty(required = false)
    @JsonPropertyDescription(
            "End date in yyyy-MM-dd format"
    )
    private String toDate;

    @JsonProperty(required = false)
    @JsonPropertyDescription(
            "Maximum number of transactions to return"
    )
    private Integer limit;
}