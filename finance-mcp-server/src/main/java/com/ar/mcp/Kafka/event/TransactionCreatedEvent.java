package com.ar.mcp.Kafka.event;

import com.ar.mcp.account.domain.CurrencyCode;
import com.ar.mcp.transaction.domain.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionCreatedEvent(
        String transactionId,
        String accountNumber,
        TransactionType transactionType,
        BigDecimal amount,
        CurrencyCode currency,
        String description,
        Instant transactionDate
) {
}