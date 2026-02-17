package com.halo.eventer.domain.home.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomeCacheService {

    private final CacheManager cacheManager;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onHomeCacheEvict(HomeCacheEvictEvent event) {
        log.debug("Evicting home cache for festivalId: {}", event.getFestivalId());
        Cache homeCache = cacheManager.getCache("home");
        if (homeCache != null) {
            homeCache.evict(event.getFestivalId());
        }
    }
}
