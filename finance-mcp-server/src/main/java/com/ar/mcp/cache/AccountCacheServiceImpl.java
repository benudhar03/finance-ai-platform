package com.ar.mcp.cache;

import com.ar.mcp.account.dto.AccountBalanceResponse;
import com.ar.mcp.account.repository.AccountRepository;
import com.ar.mcp.redis.RedisKeyBuilder;
import com.ar.mcp.redis.RedisProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountCacheServiceImpl implements AccountCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final AccountRepository accountRepository;
    private final RedisProperties redisProperties;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<AccountBalanceResponse> get(String accountNumber) {

        String cacheKey =
                RedisKeyBuilder.accountBalance(accountNumber);
        Object cachedValue =
                redisTemplate.opsForValue().get(cacheKey);

        if (cachedValue == null) {
            log.info("Redis Cache MISS. accountNumber={}", maskAccountNumber(accountNumber));
            return Optional.empty();
        }

        log.info(
                "Redis Cache HIT. accountNumber={}, cachedType={}",
                maskAccountNumber(accountNumber),
                cachedValue.getClass().getName()
        );
        try {
            AccountBalanceResponse response =
                    objectMapper.convertValue(cachedValue, AccountBalanceResponse.class);

            log.info(
                    "Cached account balance retrieved successfully. " +
                            "accountNumber={}, balance={}, currency={}",
                    maskAccountNumber(accountNumber),
                    response.balance(),
                    response.currency()
            );
            return Optional.of(response);
        } catch (IllegalArgumentException exception) {

            /*
             * Cached data cannot be converted to the expected DTO.
             * Remove the invalid cache entry so the next request
             * can retrieve fresh data from PostgreSQL.
             */
            log.warn(
                    "Invalid cached account balance. Evicting cache. " +
                            "accountNumber={}",
                    maskAccountNumber(accountNumber),
                    exception
            );
            redisTemplate.delete(cacheKey);
            return Optional.empty();
        }
    }

    @Override
    public void put(AccountBalanceResponse response) {

        String cacheKey =
                RedisKeyBuilder.accountBalance(
                        response.accountNumber()
                );

        redisTemplate.opsForValue().set(
                cacheKey,
                response,
                getCacheTtl()
        );

        log.info(
                "Account balance cached successfully. " +
                        "accountNumber={}, ttlMinutes={}",
                maskAccountNumber(response.accountNumber()),
                redisProperties.ttlMinutes()
        );
    }

    @Override
    public void evict(String accountNumber) {

        String cacheKey =
                RedisKeyBuilder.accountBalance(accountNumber);

        redisTemplate.delete(cacheKey);

        log.info(
                "Account cache evicted. accountNumber={}",
                maskAccountNumber(accountNumber)
        );
    }

    @Override
    public void reload(String accountNumber) {

        accountRepository.findByAccountNumber(accountNumber)
                .ifPresentOrElse(
                        account -> {

                            AccountBalanceResponse response =
                                    AccountBalanceResponse.from(account);

                            put(response);

                            log.info(
                                    "Account cache reloaded successfully. " +
                                            "accountNumber={}",
                                    maskAccountNumber(accountNumber)
                            );
                        },
                        () -> log.warn(
                                "Account not found while reloading cache. " +
                                        "accountNumber={}",
                                maskAccountNumber(accountNumber)
                        )
                );
    }

    @Override
    public void markForRefresh(String accountNumber) {

        redisTemplate.opsForSet().add(
                RedisKeyBuilder.accountRefreshSet(),
                accountNumber
        );

        log.debug(
                "Account marked for refresh. accountNumber={}",
                maskAccountNumber(accountNumber)
        );
    }

    @Override
    public Set<String> getAccountsMarkedForRefresh() {

        Set<Object> members =
                redisTemplate.opsForSet()
                        .members(
                                RedisKeyBuilder.accountRefreshSet()
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
                        RedisKeyBuilder.accountRefreshSet(),
                        accountNumber
                );

        log.debug(
                "Removed account refresh marker. accountNumber={}",
                maskAccountNumber(accountNumber)
        );
    }

    private Duration getCacheTtl() {

        return Duration.ofMinutes(
                redisProperties.ttlMinutes()
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
}