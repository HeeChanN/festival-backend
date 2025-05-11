package com.halo.eventer.integration.map;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

import com.halo.eventer.domain.duration.Duration;
import com.halo.eventer.domain.duration.repository.DurationRepository;
import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.dto.FestivalCreateDto;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.map.MapCategory;
import com.halo.eventer.domain.map.MapFixture;
import com.halo.eventer.domain.map.dto.map.MapCreateDto;
import com.halo.eventer.domain.map.repository.MapCategoryRepository;
import com.halo.eventer.domain.map.service.MapService;

@SpringBootTest
@Disabled
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("deploy")
public class MapBatchInsertTest {

    @Autowired
    MapService mapService;

    @Autowired
    DurationRepository durationRepository;

    @Autowired
    MapCategoryRepository mapCategoryRepository;

    @Autowired
    FestivalRepository festivalRepository;

    private Festival festival;
    private MapCategory mapCategory;
    List<Long> durationIds = new ArrayList<>();

    @BeforeEach
    void setup() {
        festival = Festival.from(new FestivalCreateDto("이름", "주소"));
        festival = festivalRepository.save(festival);
        LocalDate startDate = LocalDate.of(2025, 3, 3);
        for (int i = 1; i <= 5; i++) {
            LocalDate date = startDate.plusDays(i);
            Duration duration = new Duration(date, i, festival);
            duration = durationRepository.save(duration);
            durationIds.add(duration.getId());
        }

        mapCategory = mapCategoryRepository.save(MapCategory.of(festival, "카테고리"));
    }

    @Test
    void 지도정보생성시_N개의_DurationMap_생성_테스트() {
        MapCreateDto mapCreateDto = MapFixture.지도_생성_DTO();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        mapService.create(mapCreateDto, mapCategory.getId());
        stopWatch.stop();
        System.out.println("수행시간(ms): " + stopWatch.getTotalTimeMillis());
    }
}
