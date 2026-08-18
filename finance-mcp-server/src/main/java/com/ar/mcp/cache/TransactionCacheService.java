package com.ar.mcp.cache;

import com.ar.mcp.transaction.dto.AccountTransactionsResponse;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

public interface TransactionCacheService {

    Optional<AccountTransactionsResponse> get(
            String accountNumber,
            String fromDate,
            String toDate,
            Integer limit
    );

    void put(
            AccountTransactionsResponse response,
            String accountNumber,
            String fromDate,
            String toDate,
            Integer limit
    );

    void evict(
            String accountNumber,
            String fromDate,
            String toDate,
            Integer limit
    );

    void markForRefresh(String accountNumber);

    Set<String> getAccountsMarkedForRefresh();

    void removeRefreshMarker(String accountNumber);
}