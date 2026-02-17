package com.halo.eventer.global.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import com.halo.eventer.global.cache.CacheEvictionSubscriber;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnBean(RedisConnectionFactory.class)
@Slf4j
public class RedisMessageConfig {

    private static final String CHANNEL = "cache:evict:home";

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory, CacheEvictionSubscriber subscriber) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(subscriber, new ChannelTopic(CHANNEL));
        container.setErrorHandler(e -> {
            log.warn("Redis Pub/Sub listener error: {}", e.getMessage());
            subscriber.markDisconnected();
        });
        return container;
    }
}
