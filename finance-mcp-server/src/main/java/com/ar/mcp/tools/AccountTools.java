package com.ar.mcp.tools;

import com.ar.mcp.account.dto.AccountBalanceResponse;
import com.ar.mcp.account.service.AccountService;
import com.ar.mcp.transaction.dto.AccountTransactionsResponse;
import com.ar.mcp.transaction.service.TransactionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

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

                    IMPORTANT:
                    - If fromDate and toDate are not provided,
                      return the most recent available transactions.
                    - Never assume or invent a date range.
                    - If limit is not provided, use the service default.
                    - This operation is READ-ONLY.
                    """
    )
    public AccountTransactionsResponse getAccountTransactions(
            String accountNumber,
            String fromDate,
            String toDate,
            Integer limit) {
        log.info(
                "MCP tool invoked: getAccountTransactions, " +
                        "accountNumber={}, fromDate={}, toDate={}, limit={}",
                mask(accountNumber),
                fromDate,
                toDate,
                limit
        );
        return transactionService.getAccountTransactions(
                accountNumber,
                fromDate,
                toDate,
                limit
        );
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