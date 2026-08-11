package com.ar.mcp.transaction.service;

import com.ar.mcp.account.domain.Account;
import com.ar.mcp.account.repository.AccountRepository;
import com.ar.mcp.transaction.domain.Transaction;
import com.ar.mcp.transaction.dto.AccountTransactionsResponse;
import com.ar.mcp.transaction.dto.TransactionResponse;
import com.ar.mcp.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TransactionService {

    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LIMIT = 100;

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountTransactionsResponse getAccountTransactions(
            String accountNumber,
            String fromDate,
            String toDate,
            Integer limit) {

        validateAccountNumber(accountNumber);
        log.info(
                "Fetching transactions for account={}, fromDate={}, toDate={}, limit={}",
                mask(accountNumber),
                fromDate,
                toDate,
                limit
        );

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Account not found: " + accountNumber
                        )
                );

        int transactionLimit = normalizeLimit(limit);

        Instant from = parseFromDate(fromDate);
        Instant to = parseToDate(toDate);

        validateDateRange(from, to);

        List<Transaction> transactions;

        if (from == null && to == null) {

            transactions = transactionRepository
                    .findByAccountAccountNumberOrderByTransactionDateDesc(
                            accountNumber
                    );

        } else {

            Instant effectiveFrom =
                    from != null
                            ? from
                            : Instant.EPOCH;

            Instant effectiveTo =
                    to != null
                            ? to
                            : Instant.now();

            transactions = transactionRepository
                    .findByAccountAccountNumberAndTransactionDateBetweenOrderByTransactionDateDesc(
                            accountNumber,
                            effectiveFrom,
                            effectiveTo
                    );
        }

        List<TransactionResponse> responses = transactions.stream()
                .limit(transactionLimit)
                .map(this::toResponse)
                .toList();

        return new AccountTransactionsResponse(
                account.getAccountNumber(),
                account.getCurrency(),
                responses
        );
    }

    private TransactionResponse toResponse(
            Transaction transaction
    ) {

        return new TransactionResponse(
                transaction.getTransactionReference(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getDescription(),
                transaction.getTransactionDate()
        );
    }

    private int normalizeLimit(Integer limit) {

        if (limit == null) {
            return DEFAULT_LIMIT;
        }

        if (limit <= 0) {
            throw new IllegalArgumentException(
                    "Transaction limit must be greater than zero"
            );
        }

        return Math.min(limit, MAX_LIMIT);
    }

    private Instant parseFromDate(String date) {

        if (date == null || date.isBlank()) {
            return null;
        }

        return LocalDate.parse(date)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();
    }

    private Instant parseToDate(String date) {

        if (date == null || date.isBlank()) {
            return null;
        }

        return LocalDate.parse(date)
                .plusDays(1)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .minusNanos(1);
    }

    private void validateDateRange(Instant from, Instant to) {

        if (from != null &&
                to != null &&
                from.isAfter(to)) {

            throw new IllegalArgumentException(
                    "From date must not be after to date"
            );
        }
    }

    private void validateAccountNumber(String accountNumber) {
        if (accountNumber == null ||
                accountNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Account number must not be empty"
            );
        }

        if (!accountNumber.matches("^ACC-\\d{4,}$")) {
            throw new IllegalArgumentException(
                    "Invalid account number format"
            );
        }
    }

    private String mask(String accountNumber) {

        if (accountNumber == null ||
                accountNumber.length() <= 4) {
            return "****";
        }

        return "****" +
                accountNumber.substring(
                        accountNumber.length() - 4
                );
    }
}