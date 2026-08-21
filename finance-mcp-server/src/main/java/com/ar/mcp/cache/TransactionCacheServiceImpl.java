package com.ar.mcp.cache;

import com.ar.mcp.redis.RedisKeyBuilder;
import com.ar.mcp.redis.RedisProperties;
import com.ar.mcp.transaction.dto.AccountTransactionsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionCacheServiceImpl implements TransactionCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisProperties redisProperties;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<AccountTransactionsResponse> get(
            String accountNumber,
            String from,
            String to,
            Integer limit
    ) {

        String cacheKey =
                RedisKeyBuilder.accountTransactions(
                        accountNumber,
                        from,
                        to,
                        limit
                );

        Object cachedValue =
                redisTemplate.opsForValue().get(cacheKey);

        if (cachedValue == null) {
            log.info(
                    "Redis Cache MISS. accountNumber={}",
                    maskAccountNumber(accountNumber)
            );
            return Optional.empty();
        }

        log.info(
                "Redis Cache HIT. accountNumber={}",
                maskAccountNumber(accountNumber)
        );
        try{
            AccountTransactionsResponse response =
                    objectMapper.convertValue(
                            cachedValue,
                            AccountTransactionsResponse.class
                    );

            return Optional.of(response);
        }catch (IllegalArgumentException ex){
            log.error(
                    "Failed to deserialize cached transaction response. " +
                            "accountNumber={}, cacheKey={}, cachedType={}, cachedValue={}",
                    maskAccountNumber(accountNumber),
                    cacheKey,
                    cachedValue.getClass().getName(),
                    cachedValue,
                    ex
            );

            return Optional.empty();
        }
    }

    @Override
    public void put(
            AccountTransactionsResponse response,
            String accountNumber,
            String fromDate,
            String toDate,
            Integer limit
    ) {

        String cacheKey = RedisKeyBuilder.accountTransactions(
                accountNumber,
                fromDate,
                toDate,
                limit
        );

        redisTemplate.opsForValue().set(
                cacheKey,
                response,
                Duration.ofMinutes(
                        redisProperties.ttlMinutes()
                )
        );

        log.info(
                "Transaction response cached successfully. " +
                "accountNumber={}, transactionCount={}, ttlMinutes={}",
                maskAccountNumber(accountNumber),
                response.transactions().size(),
                redisProperties.ttlMinutes()
        );
    }

    @Override
    public void evict(
            String accountNumber,
            String fromDate,
            String toDate,
            Integer limit
    ) {

        String cacheKey = RedisKeyBuilder.accountTransactions(
                accountNumber,
                fromDate,
                toDate,
                limit
        );

        redisTemplate.delete(cacheKey);

        log.info(
                "Transaction cache evicted. accountNumber={}",
                maskAccountNumber(accountNumber)
        );
    }

    @Override
    public void markForRefresh(String accountNumber) {

        redisTemplate.opsForSet().add(
                RedisKeyBuilder.transactionRefreshSet(),
                accountNumber
        );

        log.debug(
                "Account transactions marked for refresh. accountNumber={}",
                maskAccountNumber(accountNumber)
        );
    }

    @Override
    public Set<String> getAccountsMarkedForRefresh() {

        Set<Object> members =
                redisTemplate.opsForSet()
                        .members(
                                RedisKeyBuilder.transactionRefreshSet()
                        );

        if (members == null || members.isEmpty()) {
            return Set.of();
        }

        return members.stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    @Override
    public void removeRefreshMarker(String accountNumber) {

        redisTemplate.opsForSet()
                .remove(
                        RedisKeyBuilder.transactionRefreshSet(),
                        accountNumber
                );

        log.debug(
                "Removed transaction refresh marker. accountNumber={}",
                maskAccountNumber(accountNumber)
        );
    }

    private String maskAccountNumber(String accountNumber) {

        if (accountNumber == null ||
                accountNumber.length() <= 4) {
            return "****";
        }

        return "****" +
                accountNumber.substring(
                        accountNumber.length() - 4
                );
    }

    @Override
    public void evictByAccount(String accountNumber) {

        String keyPattern =
                RedisKeyBuilder.accountTransactionsPrefix(accountNumber) + "*";

        Set<String> cacheKeys =
                redisTemplate.keys(keyPattern);

        if (cacheKeys == null || cacheKeys.isEmpty()) {

            log.info(
                    "No transaction cache entries found for accountNumber={}",
                    maskAccountNumber(accountNumber)
            );

            return;
        }

        redisTemplate.delete(cacheKeys);

        log.info("Transaction cache entries evicted. " + "accountNumber={}, entriesRemoved={}",
                maskAccountNumber(accountNumber), cacheKeys.size()
        );
    }

}