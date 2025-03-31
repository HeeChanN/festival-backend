package com.halo.eventer.domain.map.service;

import com.halo.eventer.domain.duration.Duration;
import com.halo.eventer.domain.duration.repository.DurationMapJdbcRepository;
import com.halo.eventer.domain.duration.repository.DurationRepository;
import com.halo.eventer.domain.duration.DurationMap;
import com.halo.eventer.domain.map.Map;
import com.halo.eventer.domain.map.MapCategory;
import com.halo.eventer.domain.map.dto.map.MapItemDto;
import com.halo.eventer.domain.map.dto.map.MapCreateDto;
import com.halo.eventer.domain.map.dto.map.MapResDto;
import com.halo.eventer.domain.map.dto.map.MapUpdateDto;
import com.halo.eventer.domain.map.exception.DurationIdsInvalidInputException;
import com.halo.eventer.domain.map.exception.MapCategoryNotFoundException;
import com.halo.eventer.domain.map.exception.MapNotFoundException;
import com.halo.eventer.domain.map.repository.MapCategoryRepository;
import com.halo.eventer.domain.map.repository.MapRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MapService {
    private final MapRepository mapRepository;
    private final MapCategoryRepository mapCategoryRepository;
    private final DurationRepository durationRepository;
    private final DurationMapJdbcRepository mapJdbcRepository;

    @Transactional
    public MapResDto create(MapCreateDto mapCreateDto, Long mapCategoryId){
        List<Duration> durations = getValidatedDurations(mapCreateDto.getDurationIdsToAdd());
        MapCategory mapCategory = mapCategoryRepository.findById(mapCategoryId)
                .orElseThrow(() -> new MapCategoryNotFoundException(mapCategoryId));

        Map map = Map.of(mapCreateDto, mapCategory);
        map = mapRepository.save(map);

        mapJdbcRepository.batchInsertMapDurations(map.getId(), durations);

        return MapResDto.of(map,durations);
    }

  public MapResDto getMap(Long mapId) {
    Map map = mapRepository.findByIdWithCategoryAndDuration(mapId)
            .orElseThrow(() -> new MapNotFoundException(mapId));
    List<Duration> durations = map.getDurationMaps().stream()
            .map(DurationMap::getDuration)
            .collect(Collectors.toList());
    return MapResDto.of(map, durations);
  }

  public List<MapItemDto> getMaps(Long mapCategoryId) {
        return mapRepository.findByCategoryIdWithCategoryAndDuration(mapCategoryId)
                .stream().map(MapItemDto::new).collect(Collectors.toList());
    }

    @Transactional
    public MapResDto update(Long mapId, MapUpdateDto mapUpdateDto, Long mapCategoryId) {
        MapCategory mapCategory = mapCategoryRepository.findById(mapCategoryId)
                .orElseThrow(() -> new MapCategoryNotFoundException(mapCategoryId));
        Map map = mapRepository.findByIdWithCategoryAndDuration(mapId)
                .orElseThrow(() -> new MapNotFoundException(mapId));
        List<Duration> durations = getValidatedDurations(mapUpdateDto.getDurationBinding().getIdsToAdd());

        map.updateMap(mapUpdateDto, mapCategory);
        map.updateDurations(mapUpdateDto.getDurationBinding().getIdsToAdd(),
                mapUpdateDto.getDurationBinding().getIdsToRemove(), durations);

        return MapResDto.from(map);
    }

    @Transactional
    public void delete(Long mapId){
        mapRepository.deleteById(mapId);
    }

    private List<Duration> getValidatedDurations(List<Long> durationIds) {
        List<Duration> durations = durationRepository.findAllByIdIn(durationIds);

        if (durations.size() != durationIds.size()) {
            throw new DurationIdsInvalidInputException();
        }

        return durations;
    }
}
