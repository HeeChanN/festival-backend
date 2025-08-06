package com.halo.eventer.domain.duration;

import java.time.LocalDate;

import com.halo.eventer.domain.duration.dto.DurationCreateDto;
import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class DurationFixture {

    public static final LocalDate 기본_DATE = LocalDate.of(2025, 1, 1);
    public static final int 기본_DAY = 1;

    public static DurationCreateDto 기본_Duration_생성_DTO() {
        return DurationCreateDto.builder().date(기본_DATE).dayNumber(기본_DAY).build();
    }

    public static Duration 축제_첫째_날() {
        Festival festival = FestivalFixture.축제_엔티티();
        DurationCreateDto durationCreateDto = DurationCreateDto.builder()
                .date(LocalDate.of(2025, 1, 1))
                .dayNumber(1)
                .build();
        Duration duration = Duration.of(festival, durationCreateDto);
        setField(duration, "id", 1L);
        return duration;
    }

    public static Duration 축제_둘째_날() {
        Festival festival = FestivalFixture.축제_엔티티();
        DurationCreateDto durationCreateDto = DurationCreateDto.builder()
                .date(LocalDate.of(2025, 1, 2))
                .dayNumber(2)
                .build();
        Duration duration = Duration.of(festival, durationCreateDto);
        setField(duration, "id", 2L);
        return duration;
    }
}
