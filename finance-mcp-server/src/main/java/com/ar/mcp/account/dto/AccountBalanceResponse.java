package com.ar.mcp.account.dto;


import com.ar.mcp.account.domain.Account;
import com.ar.mcp.account.domain.CurrencyCode;

import java.math.BigDecimal;

public record AccountBalanceResponse(
        String accountNumber,
        BigDecimal balance,
        CurrencyCode currency,
        String status
) {

    public static AccountBalanceResponse from(Account account) {
        return new AccountBalanceResponse(
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency(),
                account.getStatus().name()
        );
    }
}