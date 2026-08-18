package com.ar.mcp.cache;

import com.ar.mcp.account.dto.AccountBalanceResponse;

import java.util.Optional;
import java.util.Set;

public interface AccountCacheService {

    Optional<AccountBalanceResponse> get(String accountNumber);

    void put(AccountBalanceResponse response);

    void evict(String accountNumber);

    void reload(String accountNumber);

    void markForRefresh(String accountNumber);

    Set<String> getAccountsMarkedForRefresh();

    void removeRefreshMarker(String accountNumber);
}