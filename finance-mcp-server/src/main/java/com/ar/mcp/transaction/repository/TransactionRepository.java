package com.ar.mcp.transaction.repository;

import com.ar.mcp.transaction.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, String> {

    List<Transaction>
    findByAccountAccountNumberOrderByTransactionDateDesc(
            String accountNumber
    );

    List<Transaction>
    findByAccountAccountNumberAndTransactionDateBetweenOrderByTransactionDateDesc(
            String accountNumber,
            Instant from,
            Instant to
    );
}