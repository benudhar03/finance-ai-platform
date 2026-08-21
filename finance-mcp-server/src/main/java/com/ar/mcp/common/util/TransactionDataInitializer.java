package com.ar.mcp.common.util;

import com.ar.mcp.account.domain.Account;
import com.ar.mcp.account.domain.CurrencyCode;
import com.ar.mcp.account.repository.AccountRepository;
import com.ar.mcp.transaction.domain.Transaction;
import com.ar.mcp.transaction.domain.TransactionType;
import com.ar.mcp.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Order(2)
@Configuration
@RequiredArgsConstructor
public class TransactionDataInitializer {

    @Bean
    CommandLineRunner initializeTransactions(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository
    ) {

        return args -> {

            if (transactionRepository.count() > 0) {
                log.info(
                        "Finance transactions already exist. " +
                        "Skipping initialization."
                );
                return;
            }

            Account account1001 = accountRepository
                    .findByAccountNumber("ACC-1001")
                    .orElseThrow();

            Account account1002 = accountRepository
                    .findByAccountNumber("ACC-1002")
                    .orElseThrow();

            Account account1003 = accountRepository
                    .findByAccountNumber("ACC-1003")
                    .orElseThrow();

            Instant now = Instant.now();

            transactionRepository.saveAll(
                    List.of(

                            // ACC-1001
                            new Transaction(
                                    "TXN-10001",
                                    account1001,
                                    TransactionType.CREDIT,
                                    new BigDecimal("100000.00"),
                                    CurrencyCode.INR,
                                    "Salary Credit",
                                    now.minus(20, ChronoUnit.DAYS)
                            ),

                            new Transaction(
                                    "TXN-10002",
                                    account1001,
                                    TransactionType.DEBIT,
                                    new BigDecimal("15000.00"),
                                    CurrencyCode.INR,
                                    "House Rent",
                                    now.minus(18, ChronoUnit.DAYS)
                            ),

                            new Transaction(
                                    "TXN-10003",
                                    account1001,
                                    TransactionType.DEBIT,
                                    new BigDecimal("4500.00"),
                                    CurrencyCode.INR,
                                    "Electricity Bill",
                                    now.minus(10, ChronoUnit.DAYS)
                            ),

                            // ACC-1002
                            new Transaction(
                                    "TXN-10004",
                                    account1002,
                                    TransactionType.CREDIT,
                                    new BigDecimal("50000.00"),
                                    CurrencyCode.INR,
                                    "Salary Credit",
                                    now.minus(15, ChronoUnit.DAYS)
                            ),

                            new Transaction(
                                    "TXN-10005",
                                    account1002,
                                    TransactionType.DEBIT,
                                    new BigDecimal("7500.50"),
                                    CurrencyCode.INR,
                                    "Online Shopping",
                                    now.minus(12, ChronoUnit.DAYS)
                            ),

                            new Transaction(
                                    "TXN-10006",
                                    account1002,
                                    TransactionType.DEBIT,
                                    new BigDecimal("3000.00"),
                                    CurrencyCode.INR,
                                    "Utility Bill",
                                    now.minus(7, ChronoUnit.DAYS)
                            ),

                            new Transaction(
                                    "TXN-10007",
                                    account1002,
                                    TransactionType.CREDIT,
                                    new BigDecimal("25000.00"),
                                    CurrencyCode.INR,
                                    "Bonus Credit",
                                    now.minus(5, ChronoUnit.DAYS)
                            ),

                            new Transaction(
                                    "TXN-10008",
                                    account1002,
                                    TransactionType.DEBIT,
                                    new BigDecimal("2500.00"),
                                    CurrencyCode.INR,
                                    "Restaurant",
                                    now.minus(2, ChronoUnit.DAYS)
                            ),

                            // ACC-1003
                            new Transaction(
                                    "TXN-10009",
                                    account1003,
                                    TransactionType.CREDIT,
                                    new BigDecimal("1000.00"),
                                    CurrencyCode.USD,
                                    "International Transfer",
                                    now.minus(8, ChronoUnit.DAYS)
                            ),

                            new Transaction(
                                    "TXN-10010",
                                    account1003,
                                    TransactionType.DEBIT,
                                    new BigDecimal("250.00"),
                                    CurrencyCode.USD,
                                    "Online Purchase",
                                    now.minus(3, ChronoUnit.DAYS)
                            )
                    )
            );

            log.info(
                    "Demo finance transactions initialized."
            );
        };
    }
}