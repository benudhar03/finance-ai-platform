package com.ar.mcp.account.service;

import com.ar.mcp.account.dto.AccountBalanceResponse;

public interface AccountService {

    AccountBalanceResponse getAccountBalance(String accountNumber);
}