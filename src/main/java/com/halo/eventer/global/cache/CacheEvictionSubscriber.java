package com.halo.eventer.global.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnBean(RedisConnectionFactory.class)
@RequiredArgsConstructor
@Slf4j
public class CacheEvictionSubscriber implements MessageListener {

    private final DistributedCacheManager cacheManager;
    private volatile boolean wasDisconnected = false;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        if (wasDisconnected) {
            log.info("Redis Pub/Sub reconnected, clearing all local caches defensively");
            cacheManager.clearCache();
            wasDisconnected = false;
        }

        String key = new String(message.getBody());
        log.debug("Received cache eviction message for key: {}", key);
        cacheManager.evictLocal(key);
    }

    public void markDisconnected() {
        this.wasDisconnected = true;
    }
}
