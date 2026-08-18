package com.ar.mcp.redis;

import java.time.Instant;

public final class RedisKeyBuilder {

    private static final String ACCOUNT_BALANCE_PREFIX = "finance:account:balance:";
    private static final String ACCOUNT_REFRESH_SET = "finance:account:balance:refresh";

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

            return "finance:account:transactions:"
                    + accountNumber
                    + ":recent:"
                    + transactionLimit;
        }

        return "finance:account:transactions:"
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
}