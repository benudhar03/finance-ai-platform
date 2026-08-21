package com.ar.mcp.transaction.dto;

import com.ar.mcp.account.domain.CurrencyCode;
import com.ar.mcp.transaction.domain.TransactionType;

import java.math.BigDecimal;

public record CreateTransactionRequest(
        String accountNumber,
        TransactionType transactionType,
        BigDecimal amount,
        CurrencyCode currency,
        String description
) {
}