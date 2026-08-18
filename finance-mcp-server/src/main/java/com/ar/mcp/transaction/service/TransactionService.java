package com.ar.mcp.transaction.service;

import com.ar.mcp.account.domain.Account;
import com.ar.mcp.account.repository.AccountRepository;
import com.ar.mcp.cache.TransactionCacheService;
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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionCacheService transactionCacheService;

    public AccountTransactionsResponse getAccountTransactions(
            String accountNumber,
            String fromDate,
            String toDate,
            Integer limit) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Account not found: " + accountNumber
                        )
                );

        int transactionLimit =
                limit == null || limit <= 0
                        ? 10
                        : Math.min(limit, 100);

        /*
         * Redis lookup.
         *
         * Keep fromDate/toDate as String here because these are
         * the MCP-friendly values and are also used to construct
         * the cache key.
         */
        Optional<AccountTransactionsResponse> cached =
                transactionCacheService.get(
                        accountNumber,
                        fromDate,
                        toDate,
                        transactionLimit
                );

        if (cached.isPresent()) {
            log.info("Returning transactions from Redis. accountNumber={}",
                    maskAccountNumber(accountNumber));
            return cached.get();
        }
        List<Transaction> transactions;

        //Latest transactions
        if (fromDate == null && toDate == null) {

            transactions = transactionRepository
                    .findByAccountAccountNumberOrderByTransactionDateDesc(
                            accountNumber
                    )
                    .stream()
                    .limit(transactionLimit)
                    .toList();

        } else {
            //Date range transactions
            if (fromDate == null || toDate == null) {

                throw new IllegalArgumentException(
                        "Both fromDate and toDate must be provided."
                );
            }

            LocalDate fromLocalDate = parseDate(fromDate);
            LocalDate toLocalDate = parseDate(toDate);

            if (fromLocalDate.isAfter(toLocalDate)) {

                throw new IllegalArgumentException(
                        "fromDate cannot be after toDate."
                );
            }

            /*
             * Convert the MCP-friendly dates into Instants
             * only when querying PostgreSQL.
             */
            Instant from = fromLocalDate
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant();

            /*
             * Include the complete toDate.
             *
             * Example:
             * 2026-08-10
             *
             * becomes:
             * 2026-08-10T23:59:59.999999999Z
             */
            Instant to = toLocalDate
                    .plusDays(1)
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant()
                    .minusNanos(1);

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

        /*
         * Convert database entities into the DTO that we expose
         * through MCP and store in Redis.
         */
        AccountTransactionsResponse response =
                AccountTransactionsResponse.from(
                        account,
                        transactions
                );

        /*
         * Store the complete response in Redis.
         */
        transactionCacheService.put(
                response,
                accountNumber,
                fromDate,
                toDate,
                transactionLimit
        );
        return response;
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException ex) {

            throw new IllegalArgumentException(
                    "Invalid date format. Expected yyyy-MM-dd."
            );
        }
    }

    private String maskAccountNumber(String accountNumber) {

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