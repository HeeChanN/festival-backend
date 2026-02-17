package com.halo.eventer.global.cache;

import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DistributedCacheManager {

    private final Cache<String, Object> cache;
    private final CacheEvictionPublisher publisher;

    public DistributedCacheManager(@Nullable CacheEvictionPublisher publisher) {
        this.publisher = publisher;
        this.cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(Duration.ofHours(1))
                .recordStats()
                .build();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Supplier<T> loader) {
        return (T) cache.get(key, k -> loader.get());
    }

    public void invalidate(String key) {
        cache.invalidate(key);
        if (publisher != null) {
            publisher.publish(key);
        }
    }

    public void evictLocal(String key) {
        cache.invalidate(key);
    }

    public void clearCache() {
        cache.invalidateAll();
        log.info("All local cache entries cleared");
    }

    public Cache<String, Object> getCaffeineCache() {
        return cache;
    }
}
