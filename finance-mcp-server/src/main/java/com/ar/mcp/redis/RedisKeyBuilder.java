package com.ar.mcp.redis;

import java.time.Instant;

public final class RedisKeyBuilder {

    private static final String ACCOUNT_BALANCE_PREFIX = "finance:account:balance:";
    private static final String ACCOUNT_REFRESH_SET = "finance:account:balance:refresh";
    private static final String ACCOUNT_TRANSACTIONS_PREFIX = "finance:account:transactions:";

    private RedisKeyBuilder() {
    }

    public static String accountBalance(String accountNumber) {
        return ACCOUNT_BALANCE_PREFIX + accountNumber;
    }
    public static String accountRefreshSet() {
        return ACCOUNT_REFRESH_SET;
    }

    public static String accountTransactions(
            String accountNumber,
            String fromDate,
            String toDate,
            Integer limit) {

        if (fromDate == null && toDate == null) {

            int transactionLimit =
                    limit == null || limit <= 0
                            ? 10
                            : Math.min(limit, 100);

            return ACCOUNT_TRANSACTIONS_PREFIX
                    + accountNumber
                    + ":recent:"
                    + transactionLimit;
        }

        return ACCOUNT_TRANSACTIONS_PREFIX
                + accountNumber
                + ":"
                + fromDate
                + ":"
                + toDate
                + ":"
                + limit;
    }

    public static String transactionRefreshSet() {
        return "finance:account:transactions:refresh";
    }

    public static String accountTransactionsPrefix(String accountNumber) {
        return ACCOUNT_TRANSACTIONS_PREFIX + accountNumber + ":";
    }
}