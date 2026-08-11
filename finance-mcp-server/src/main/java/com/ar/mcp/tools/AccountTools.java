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
                    - account number

                    Optional:
                    - from date in ISO-8601 format (yyyy-MM-dd)
                    - to date in ISO-8601 format (yyyy-MM-dd)
                    - maximum number of transactions to return

                    If no dates are provided, the most recent transactions
                    are returned.

                    If no limit is provided, the service uses its default limit.

                    This is a READ-ONLY operation.
                    It does not modify account data or create transactions.
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