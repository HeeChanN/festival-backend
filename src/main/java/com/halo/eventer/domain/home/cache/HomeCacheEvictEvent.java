package com.halo.eventer.domain.home.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HomeCacheEvictEvent {

    private final Long festivalId;
}
