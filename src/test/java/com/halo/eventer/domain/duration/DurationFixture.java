package com.halo.eventer.domain.duration;

import java.time.LocalDate;

import com.halo.eventer.domain.duration.dto.DurationCreateDto;
import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.map.Map;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class DurationFixture {

    public static final LocalDate 기본_DATE = LocalDate.of(2025, 1, 1);
    public static final int 기본_DAY = 1;

    public static DurationCreateDto 기본_Duration_생성_DTO() {
        return DurationCreateDto.builder().date(기본_DATE).day(기본_DAY).build();
    }

    public static DurationCreateDto 커스텀_Duration_생성_DTO(LocalDate date, int dayNumber) {
        return DurationCreateDto.builder().date(date).day(dayNumber).build();
    }

    public static Duration Duration_엔티티() {
        Festival festival = FestivalFixture.축제_엔티티();
        DurationCreateDto durationCreateDto = 기본_Duration_생성_DTO();
        Duration duration = Duration.of(festival, durationCreateDto);
        setField(duration, "id", 1L);
        return duration;
    }

    public static DurationMap DurationMap_엔티티(Map map, Duration duration) {
        DurationMap durationMap = DurationMap.of(duration, map);
        setField(durationMap, "id", 1L);
        return durationMap;
    }
}
