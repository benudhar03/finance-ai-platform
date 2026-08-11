package com.ar.mcp.transaction.dto;

import com.ar.mcp.account.domain.CurrencyCode;

import java.util.List;

public record AccountTransactionsResponse(
        String accountNumber,
        CurrencyCode currency,
        List<TransactionResponse> transactions
) {
}