package com.ar.mcp.transaction.dto;

import com.ar.mcp.account.domain.CurrencyCode;
import com.ar.mcp.transaction.domain.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        String transactionReference,
        TransactionType type,
        BigDecimal amount,
        CurrencyCode currency,
        String description,
        Instant transactionDate
) {
}