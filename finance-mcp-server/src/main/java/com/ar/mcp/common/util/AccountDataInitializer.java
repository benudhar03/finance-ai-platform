package com.ar.mcp.common.util;

import com.ar.mcp.account.domain.Account;
import com.ar.mcp.account.domain.AccountStatus;
import com.ar.mcp.account.domain.CurrencyCode;
import com.ar.mcp.account.dto.AccountBalanceResponse;
import com.ar.mcp.account.repository.AccountRepository;
import com.ar.mcp.cache.AccountCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Order(1)
@Configuration
public class AccountDataInitializer {

    @Bean
    CommandLineRunner initializeAccounts(
            AccountRepository accountRepository, AccountCacheService accountCacheService) {

        return args -> {
            if (accountRepository.count() > 0) {
                log.info("Accounts already exist. Skipping account initialization.");
                return;
            }
            List<Account> accounts = List.of(
                    new Account(
                            "ACC-1001",
                            CurrencyCode.INR,
                            new BigDecimal("150000.00"),
                            AccountStatus.ACTIVE
                    ),
                    new Account(
                            "ACC-1002",
                            CurrencyCode.EUR,
                            new BigDecimal("75000.50"),
                            AccountStatus.ACTIVE
                    ),
                    new Account(
                            "ACC-1003",
                            CurrencyCode.USD,
                            new BigDecimal("4500.00"),
                            AccountStatus.ACTIVE
                    ),
                    new Account(
                            "ACC-1004",
                            CurrencyCode.GBP,
                            new BigDecimal("5500.00"),
                            AccountStatus.ACTIVE
                    )
            );
            List<Account> savedAccounts =
                    accountRepository.saveAll(accounts);
            log.info("Demo finance accounts initialized. count={}", savedAccounts.size());

            /*
             * Warm Redis cache with the initial account data.
             */
            savedAccounts.forEach(account -> {
                AccountBalanceResponse response = AccountBalanceResponse.from(account);
                accountCacheService.put(response);
            });
            log.info("Demo finance account cache initialized in Redis. count={}", savedAccounts.size()
            );
        };
    }
}