package com.ar.mcp.account.service;

import com.ar.mcp.account.domain.Account;
import com.ar.mcp.account.dto.AccountBalanceResponse;
import com.ar.mcp.account.repository.AccountRepository;
import com.ar.mcp.common.exception.AccountNotFoundException;
import com.ar.mcp.common.exception.InvalidAccountException;
import com.ar.mcp.cache.AccountCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountCacheService accountCacheService;

    @Override
    public AccountBalanceResponse getAccountBalance(String accountNumber) {

        validateAccountNumber(accountNumber);
        log.info("Fetching account balance, accountNumber={}", maskAccountNumber(accountNumber));

        Optional<AccountBalanceResponse> cached = accountCacheService.get(accountNumber);
        if (cached.isPresent()){
            log.info("Cache Hit AccountNumber: {}", maskAccountNumber(accountNumber));
            return cached.get();
        }
        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException(accountNumber)
                );
        log.info("Cache Miss AccountNumber: {}", maskAccountNumber(accountNumber));
        AccountBalanceResponse response = AccountBalanceResponse.from(account);
        accountCacheService.put(response);
        return response;
    }

    private void validateAccountNumber(String accountNumber) {

        if (accountNumber == null || accountNumber.isBlank()) {
            throw new InvalidAccountException("Account number must not be empty"
            );
        }
        if (!accountNumber.matches("^ACC-\\d{4,}$")) {
            throw new InvalidAccountException("Invalid account number format"
            );
        }
    }

    private String maskAccountNumber(String accountNumber) {

        if (accountNumber.length() <= 4) {
            return "****";
        }
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }
}
