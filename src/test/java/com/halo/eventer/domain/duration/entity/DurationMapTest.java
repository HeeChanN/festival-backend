package com.halo.eventer.domain.duration.entity;

import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.duration.Duration;
import com.halo.eventer.domain.duration.DurationMap;
import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.dto.FestivalCreateDto;
import com.halo.eventer.domain.map.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class DurationMapTest {
    private FestivalCreateDto festivalCreateDto;
    private Festival festival;

    @Test
    void DurationMap_객체_생성() {
        // when
        DurationMap durationMap = DurationMap.of(new Duration(), new Map());

        // then
        assertThat(durationMap).isNotNull();
        assertThat(durationMap.getDuration()).isInstanceOf(Duration.class);
        assertThat(durationMap.getMap()).isInstanceOf(Map.class);
    }
}
