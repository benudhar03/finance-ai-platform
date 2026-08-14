package com.ar.mcp.transaction.service;

import com.ar.mcp.account.domain.Account;
import com.ar.mcp.account.repository.AccountRepository;
import com.ar.mcp.transaction.domain.Transaction;
import com.ar.mcp.transaction.dto.AccountTransactionsResponse;
import com.ar.mcp.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public AccountTransactionsResponse getAccountTransactions(String accountNumber, String fromDate, String toDate, Integer limit) {

        // 1. Validate account
        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Account not found: " + accountNumber
                        )
                );

        // 2. Default / validate limit
        int transactionLimit =
                limit == null || limit <= 0
                        ? 10
                        : Math.min(limit, 100);

        List<Transaction> transactions;

        // 3. No date range → latest transactions
        if (fromDate == null && toDate == null) {

            transactions = transactionRepository
                    .findByAccountAccountNumberOrderByTransactionDateDesc(
                            accountNumber
                    )
                    .stream()
                    .limit(transactionLimit)
                    .toList();

        } else {

            // 4. Date range provided → both dates required
            if (fromDate == null || toDate == null) {
                throw new IllegalArgumentException(
                        "Both fromDate and toDate must be provided."
                );
            }

            Instant from = parseDate(fromDate);
            Instant to = parseDate(toDate);

            if (from.isAfter(to)) {
                throw new IllegalArgumentException("fromDate cannot be after toDate.");
            }

            transactions = transactionRepository
                    .findByAccountAccountNumberAndTransactionDateBetweenOrderByTransactionDateDesc(
                            accountNumber,
                            from,
                            to
                    )
                    .stream()
                    .limit(transactionLimit)
                    .toList();
        }

        return AccountTransactionsResponse.from(account, transactions);
    }

    private Instant parseDate(String date) {

        try {
            return LocalDate
                    .parse(date)
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant();

        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(
                    "Invalid date format. Expected yyyy-MM-dd."
            );
        }
    }
}