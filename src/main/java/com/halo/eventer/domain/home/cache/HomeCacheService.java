package com.halo.eventer.domain.home.cache;

import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.halo.eventer.global.cache.DistributedCacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomeCacheService {

    private static final String HOME_CACHE_KEY_PREFIX = "home:";

    private final DistributedCacheManager distributedCacheManager;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onHomeCacheEvict(HomeCacheEvictEvent event) {
        String key = HOME_CACHE_KEY_PREFIX + event.getFestivalId();
        log.debug("Evicting home cache for festivalId: {}", event.getFestivalId());
        distributedCacheManager.invalidate(key);
    }
}
