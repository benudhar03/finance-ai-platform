package com.ar.mcp.transaction.dto;

import com.ar.mcp.account.domain.Account;
import com.ar.mcp.account.domain.CurrencyCode;
import com.ar.mcp.transaction.domain.Transaction;

import java.util.List;

public record AccountTransactionsResponse(
        String accountNumber,
        CurrencyCode currency,
        List<TransactionResponse> transactions
) {

    public static AccountTransactionsResponse from(
            Account account,
            List<Transaction> transactions) {

        List<TransactionResponse> transactionResponses =
                transactions.stream()
                        .map(transaction -> new TransactionResponse(
                                transaction.getTransactionReference(),
                                transaction.getType(),
                                transaction.getAmount(),
                                CurrencyCode.valueOf(
                                        transaction.getCurrency().name()
                                ),
                                transaction.getDescription(),
                                transaction.getTransactionDate()
                        ))
                        .toList();

        return new AccountTransactionsResponse(
                account.getAccountNumber(),
                CurrencyCode.valueOf(account.getCurrency().name()),
                transactionResponses
        );
    }
}