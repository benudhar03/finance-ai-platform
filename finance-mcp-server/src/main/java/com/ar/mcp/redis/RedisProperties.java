package com.ar.mcp.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.redis")
public record RedisProperties(
        long ttlMinutes,
        long refreshIntervalMs
) {
}