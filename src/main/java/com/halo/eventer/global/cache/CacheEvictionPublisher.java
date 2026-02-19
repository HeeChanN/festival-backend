package com.halo.eventer.global.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnProperty(name = "spring.data.redis.host")
@RequiredArgsConstructor
@Slf4j
public class CacheEvictionPublisher {

    private static final String CHANNEL = "cache:evict:home";

    private final StringRedisTemplate redisTemplate;

    public void publish(String key) {
        try {
            redisTemplate.convertAndSend(CHANNEL, key);
        } catch (Exception e) {
            log.warn("Failed to publish cache eviction for key: {}", key, e);
        }
    }
}
