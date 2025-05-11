package com.halo.eventer.domain.map.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.duration.Duration;
import com.halo.eventer.domain.duration.DurationMap;
import com.halo.eventer.domain.duration.repository.DurationMapJdbcRepository;
import com.halo.eventer.domain.duration.repository.DurationRepository;
import com.halo.eventer.domain.map.Map;
import com.halo.eventer.domain.map.MapCategory;
import com.halo.eventer.domain.map.MapCategoryFixture;
import com.halo.eventer.domain.map.MapFixture;
import com.halo.eventer.domain.map.dto.map.MapCreateDto;
import com.halo.eventer.domain.map.dto.map.MapItemDto;
import com.halo.eventer.domain.map.dto.map.MapResDto;
import com.halo.eventer.domain.map.dto.map.MapUpdateDto;
import com.halo.eventer.domain.map.exception.MapCategoryNotFoundException;
import com.halo.eventer.domain.map.exception.MapNotFoundException;
import com.halo.eventer.domain.map.repository.MapCategoryRepository;
import com.halo.eventer.domain.map.repository.MapRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class MapServiceTest {

    @Mock
    private MapRepository mapRepository;

    @Mock
    private MapCategoryRepository mapCategoryRepository;

    @Mock
    private DurationRepository durationRepository;

    @Mock
    private DurationMapJdbcRepository durationMapJdbcRepository;

    @InjectMocks
    private MapService mapService;

    private MapCreateDto mapCreateDto;
    private MapUpdateDto mapUpdateDto;
    private Map map;
    private MapCategory mapCategory;
    private Long mapCategoryId = 1L;

    @BeforeEach
    void setUp() {
        mapCreateDto = MapFixture.지도_생성_DTO();
        mapUpdateDto = MapFixture.지도_수정_DTO();
        mapCategory = MapCategoryFixture.지도카테고리_엔티티();
        map = MapFixture.지도_엔티티(mapCreateDto, mapCategory, 1);
    }

    @Test
    void 지도_생성_테스트() {
        // given
        List<Duration> durations =
                map.getDurationMaps().stream().map(DurationMap::getDuration).collect(Collectors.toList());
        given(durationRepository.findAllByIdIn(mapCreateDto.getDurationIdsToAdd()))
                .willReturn(durations);
        given(mapCategoryRepository.findById(mapCategoryId)).willReturn(Optional.of(mapCategory));
        given(mapRepository.save(any())).willReturn(map);
        doNothing().when(durationMapJdbcRepository).batchInsertMapDurations(any(), any());

        // when
        MapResDto result = mapService.create(mapCreateDto, mapCategoryId);

        // then
        assertThat(result.getDurations()).hasSize(1);
        assertResultEqualsCreateDto(result, mapCreateDto);
    }

    @Test
    void 지도_생성_mapCategory_없을때_예외() {
        // given
        List<Duration> durations =
                map.getDurationMaps().stream().map(DurationMap::getDuration).collect(Collectors.toList());
        given(durationRepository.findAllByIdIn(mapCreateDto.getDurationIdsToAdd()))
                .willReturn(durations);
        given(mapCategoryRepository.findById(mapCategoryId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mapService.create(mapCreateDto, mapCategoryId))
                .isInstanceOf(MapCategoryNotFoundException.class);
    }

    @Test
    void 지도_단일_조회() {
        // given
        final long id = 1L;
        given(mapRepository.findByIdWithCategoryAndDuration(id)).willReturn(Optional.of(map));

        // when
        MapResDto result = mapService.getMap(id);

        // then
        assertThat(result.getDurations()).hasSize(1);
        assertResultEqualsCreateDto(result, mapCreateDto);
    }

    @Test
    void 지도_조회_map_존재하지_않을_경우() {
        // given
        final long mapCategoryId = 1L;
        given(mapRepository.findByIdWithCategoryAndDuration(mapCategoryId)).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> mapService.getMap(mapCategoryId)).isInstanceOf(MapNotFoundException.class);
    }

    @Test
    void 지도_categoryId_로_리스트_조회() {
        // given
        given(mapRepository.findByCategoryIdWithCategoryAndDuration(mapCategoryId))
                .willReturn(List.of(map));

        // when
        List<MapItemDto> result = mapService.getMaps(mapCategoryId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategoryName())
                .isEqualTo(map.getMapCategory().getCategoryName());
        assertThat(result.get(0).getIcon()).isEqualTo(map.getIcon());
        assertThat(result.get(0).getName()).isEqualTo(map.getName());
    }

    @Test
    void 지도_수정_테스트() {
        // given
        final long mapId = 1L;
        given(mapCategoryRepository.findById(mapCategoryId)).willReturn(Optional.of(mapCategory));
        given(mapRepository.findByIdWithCategoryAndDuration(mapId)).willReturn(Optional.of(map));
        Duration duration = new Duration();
        setField(duration, "id", 5L);
        List<Duration> durations = List.of(duration);
        given(durationRepository.findAllByIdIn(mapCreateDto.getDurationIdsToAdd()))
                .willReturn(durations);

        // when
        MapResDto result = mapService.update(mapId, mapUpdateDto, mapCategoryId);

        // then
        assertThat(result.getDurations()).hasSize(1);
        assertThat(result.getDurations().get(0).getDurationId()).isEqualTo(5L);
        assertResultEqualsUpdateDto(result, mapUpdateDto);
    }

    @Test
    void 지도_수정_MapCategory_없을때_예외() {
        // given
        final long mapId = 1L;
        given(mapCategoryRepository.findById(mapCategoryId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mapService.update(mapId, mapUpdateDto, mapCategoryId))
                .isInstanceOf(MapCategoryNotFoundException.class);
    }

    @Test
    void 지도_수정_Map_없을때() {
        // given
        final long mapId = 1L;
        given(mapCategoryRepository.findById(mapCategoryId)).willReturn(Optional.of(mapCategory));
        given(mapRepository.findByIdWithCategoryAndDuration(mapId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mapService.update(mapId, mapUpdateDto, mapCategoryId))
                .isInstanceOf(MapNotFoundException.class);
    }

    @Test
    void 지도_삭제_테스트() {
        // given
        final long mapId = 1L;
        doNothing().when(mapRepository).deleteById(mapId);

        // when
        mapService.delete(mapId);

        // then
        verify(mapRepository, times(1)).deleteById(mapId);
    }

    private void assertResultEqualsCreateDto(MapResDto result, MapCreateDto createDto) {
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

    private void assertResultEqualsUpdateDto(MapResDto result, MapUpdateDto updateDto) {
        assertThat(result.getName()).isEqualTo(updateDto.getName());
        assertThat(result.getSummary()).isEqualTo(updateDto.getSummary());
        assertThat(result.getContent()).isEqualTo(updateDto.getContent());
        assertThat(result.getThumbnail()).isEqualTo(updateDto.getThumbnail());
        assertThat(result.getIcon()).isEqualTo(updateDto.getIcon());
        assertThat(result.getOperationInfo().getHours())
                .isEqualTo(updateDto.getOperationInfo().getHours());
        assertThat(result.getOperationInfo().getType())
                .isEqualTo(updateDto.getOperationInfo().getType());
        assertThat(result.getLocationInfo().getAddress())
                .isEqualTo(updateDto.getLocationInfo().getAddress());
        assertThat(result.getLocationInfo().getLatitude())
                .isEqualTo(updateDto.getLocationInfo().getLatitude());
        assertThat(result.getLocationInfo().getLongitude())
                .isEqualTo(updateDto.getLocationInfo().getLongitude());
        assertThat(result.getButtonInfo().getImage())
                .isEqualTo(updateDto.getButtonInfo().getImage());
        assertThat(result.getButtonInfo().getName())
                .isEqualTo(updateDto.getButtonInfo().getName());
        assertThat(result.getButtonInfo().getUrl())
                .isEqualTo(updateDto.getButtonInfo().getUrl());
    }
}
