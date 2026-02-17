package com.halo.eventer.domain.home.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.halo.eventer.global.cache.DistributedCacheManager;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/cache")
@RequiredArgsConstructor
public class CacheAdminController {

    private static final String HOME_CACHE_KEY_PREFIX = "home:";

    private final DistributedCacheManager distributedCacheManager;

    @PostMapping("/evict/home/{festivalId}")
    public ResponseEntity<Void> evictHomeCacheByFestival(@PathVariable Long festivalId) {
        distributedCacheManager.invalidate(HOME_CACHE_KEY_PREFIX + festivalId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/evict/home")
    public ResponseEntity<Void> evictAllHomeCache() {
        distributedCacheManager.clearCache();
        return ResponseEntity.noContent().build();
    }
}
