package com.halo.eventer.domain.duration.entity;

import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.duration.Duration;
import com.halo.eventer.domain.duration.DurationFixture;
import com.halo.eventer.domain.duration.DurationMap;
import com.halo.eventer.domain.map.Map;
import com.halo.eventer.domain.map.MapFixture;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class DurationMapTest {
    private Duration duration = DurationFixture.Duration_엔티티();
    private Map map = MapFixture.기본_지도_엔티티();

    @Test
    void DurationMap_객체_생성() {
        // when
        DurationMap result = DurationMap.of(duration, map);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getDuration()).isEqualTo(duration);
        assertThat(result.getMap()).isEqualTo(map);
    }
}
