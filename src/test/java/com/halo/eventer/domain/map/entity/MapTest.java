package com.halo.eventer.domain.map.entity;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.duration.Duration;
import com.halo.eventer.domain.map.Map;
import com.halo.eventer.domain.map.MapCategory;
import com.halo.eventer.domain.map.MapCategoryFixture;
import com.halo.eventer.domain.map.MapFixture;
import com.halo.eventer.domain.map.dto.map.MapCreateDto;
import com.halo.eventer.domain.map.dto.map.MapUpdateDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class MapTest {

    private Map map;
    private MapCreateDto mapCreateDto;
    private MapUpdateDto mapUpdateDto;
    private MapCategory mapCategory;

    @BeforeEach
    public void setUp() {
        mapCreateDto = MapFixture.지도_생성_DTO();
        mapUpdateDto = MapFixture.지도_수정_DTO();
        mapCategory = MapCategoryFixture.지도카테고리_엔티티();
        map = MapFixture.지도_엔티티(mapCreateDto, mapCategory, 4);
    }

    @Test
    void 지도_생성_테스트() {
        // then
        assertResultEqualsCreateDto(map, mapCreateDto);
        assertThat(map.getDurationMaps()).hasSize(4);
    }

    @Test
    void 지도_수정_테스트() {
        // given
        Duration duration = new Duration();
        setField(duration, "id", 5L);

        // when
        map.updateMap(mapUpdateDto, mapCategory, List.of(duration));

        // then
        assertResultEqualsUpdateDto(map, mapUpdateDto);
        assertThat(map.getDurationMaps()).hasSize(4);
    }

    private void assertResultEqualsCreateDto(Map result, MapCreateDto createDto) {
        assertThat(result.getName()).isEqualTo(createDto.getName());
        assertThat(result.getSummary()).isEqualTo(createDto.getSummary());
        assertThat(result.getContent()).isEqualTo(createDto.getContent());
        assertThat(result.getThumbnail()).isEqualTo(createDto.getThumbnail());
        assertThat(result.getIcon()).isEqualTo(createDto.getIcon());
        assertThat(result.getOperationInfo().getHours())
                .isEqualTo(createDto.getOperationInfo().getHours());
        assertThat(result.getOperationInfo().getType())
                .isEqualTo(createDto.getOperationInfo().getType());
        assertThat(result.getLocationInfo().getAddress())
                .isEqualTo(createDto.getLocationInfo().getAddress());
        assertThat(result.getLocationInfo().getLatitude())
                .isEqualTo(createDto.getLocationInfo().getLatitude());
        assertThat(result.getLocationInfo().getLongitude())
                .isEqualTo(createDto.getLocationInfo().getLongitude());
        assertThat(result.getButtonInfo().getImage())
                .isEqualTo(createDto.getButtonInfo().getImage());
        assertThat(result.getButtonInfo().getName())
                .isEqualTo(createDto.getButtonInfo().getName());
        assertThat(result.getButtonInfo().getUrl())
                .isEqualTo(createDto.getButtonInfo().getUrl());
    }

    private void assertResultEqualsUpdateDto(Map result, MapUpdateDto mapUpdateDto) {
        assertThat(result.getName()).isEqualTo(mapUpdateDto.getName());
        assertThat(result.getSummary()).isEqualTo(mapUpdateDto.getSummary());
        assertThat(result.getContent()).isEqualTo(mapUpdateDto.getContent());
        assertThat(result.getThumbnail()).isEqualTo(mapUpdateDto.getThumbnail());
        assertThat(result.getIcon()).isEqualTo(mapUpdateDto.getIcon());
        assertThat(result.getOperationInfo().getHours())
                .isEqualTo(mapUpdateDto.getOperationInfo().getHours());
        assertThat(result.getOperationInfo().getType())
                .isEqualTo(mapUpdateDto.getOperationInfo().getType());
        assertThat(result.getLocationInfo().getAddress())
                .isEqualTo(mapUpdateDto.getLocationInfo().getAddress());
        assertThat(result.getLocationInfo().getLatitude())
                .isEqualTo(mapUpdateDto.getLocationInfo().getLatitude());
        assertThat(result.getLocationInfo().getLongitude())
                .isEqualTo(mapUpdateDto.getLocationInfo().getLongitude());
        assertThat(result.getButtonInfo().getImage())
                .isEqualTo(mapUpdateDto.getButtonInfo().getImage());
        assertThat(result.getButtonInfo().getName())
                .isEqualTo(mapUpdateDto.getButtonInfo().getName());
        assertThat(result.getButtonInfo().getUrl())
                .isEqualTo(mapUpdateDto.getButtonInfo().getUrl());
    }
}
