package com.halo.eventer.domain.duration.entity;

import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.duration.Duration;
import com.halo.eventer.domain.duration.DurationFixture;
import com.halo.eventer.domain.duration.dto.DurationCreateDto;
import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class DurationTest {

    @Test
    void Duration_생성_테스트() {
        // given
        Festival festival = FestivalFixture.축제_엔티티();
        DurationCreateDto durationCreateDto = DurationFixture.기본_Duration_생성_DTO();

        // when
        Duration result = Duration.of(festival, durationCreateDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getFestival()).isEqualTo(festival);
        assertThat(result.getDate()).isEqualTo(durationCreateDto.getDate());
        assertThat(result.getDayNumber()).isEqualTo(durationCreateDto.getDayNumber());
    }
}
