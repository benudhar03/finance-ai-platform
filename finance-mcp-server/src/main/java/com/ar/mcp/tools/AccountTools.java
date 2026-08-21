package com.ar.mcp.tools;

import com.ar.mcp.account.domain.CurrencyCode;
import com.ar.mcp.account.dto.AccountBalanceResponse;
import com.ar.mcp.account.service.AccountService;
import com.ar.mcp.transaction.domain.TransactionType;
import com.ar.mcp.transaction.dto.AccountTransactionsRequest;
import com.ar.mcp.transaction.dto.AccountTransactionsResponse;
import com.ar.mcp.transaction.dto.CreateTransactionRequest;
import com.ar.mcp.transaction.dto.TransactionResponse;
import com.ar.mcp.transaction.service.TransactionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountTools {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @PostConstruct
    public void init() {
        log.info("========== ACCOUNT TOOLS CREATED ==========");
        log.info("MCP tool registered: getAccountBalance");
        log.info("MCP tool registered: getAccountTransactions");
    }

    @Tool(
            name = "getAccountBalance",
            description = """
                    Retrieves the current balance of a financial account.

                    Requires a valid account number.

                    This is a READ-ONLY operation.
                    It does not modify the account or create any transaction.
                    """
    )
    public AccountBalanceResponse getAccountBalance(String accountNumber) {
        log.info("MCP tool invoked: getAccountBalance, accountNumber={}", mask(accountNumber));
        return accountService.getAccountBalance(accountNumber);
    }

    @Tool(
            name = "getAccountTransactions",
            description = """
                Retrieves transactions for a financial account.

                Required:
                - accountNumber

                Optional:
                - fromDate in yyyy-MM-dd format
                - toDate in yyyy-MM-dd format
                - limit for maximum number of transactions

                If fromDate and toDate are not provided,
                return the most recent available transactions.

                Never assume or invent a date range.

                If limit is not provided,
                use the service default.

                This operation is READ-ONLY.
                """
    )
    public AccountTransactionsResponse getAccountTransactions(AccountTransactionsRequest request) {

        log.info(
                "MCP tool invoked: getAccountTransactions, " +
                        "accountNumber={}, fromDate={}, toDate={}, limit={}",
                mask(request.getAccountNumber()),
                request.getFromDate(),
                request.getToDate(),
                request.getLimit()
        );

        return transactionService.getAccountTransactions(
                request.getAccountNumber(),
                request.getFromDate(),
                request.getToDate(),
                request.getLimit()
        );
    }

    @Tool(
            name = "createTransaction",
            description = "Creates a credit or debit transaction for a financial account."
    )
    public TransactionResponse createTransaction(
            @ToolParam(description = "Account number, for example ACC-1001")
            String accountNumber,

            @ToolParam(description = "Transaction type: CREDIT or DEBIT")
            TransactionType transactionType,

            @ToolParam(description = "Transaction amount")
            BigDecimal amount,

            @ToolParam(description = "Transaction currency: INR, USD, EUR, or GBP")
            CurrencyCode currency,

            @ToolParam(description = "Description of the transaction")
            String description
    ) {
        CreateTransactionRequest request =
                new CreateTransactionRequest(
                        accountNumber,
                        transactionType,
                        amount,
                        currency,
                        description
                );

        return transactionService.createTransaction(request);
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