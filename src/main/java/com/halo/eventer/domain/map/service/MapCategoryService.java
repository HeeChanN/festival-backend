package com.halo.eventer.domain.map.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.map.MapCategory;
import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryImageDto;
import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryResDto;
import com.halo.eventer.domain.map.exception.MapCategoryNotFoundException;
import com.halo.eventer.domain.map.repository.MapCategoryRepository;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.global.util.DisplayOrderUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MapCategoryService {

    private final MapCategoryRepository mapCategoryRepository;
    private final FestivalRepository festivalRepository;

    @Transactional
    public void create(Long festivalId, String categoryName) {
        Festival festival = festivalRepository
                .findByIdWithMapCategories(festivalId)
                .orElseThrow(() -> new FestivalNotFoundException(festivalId));
        mapCategoryRepository.save(MapCategory.of(festival, categoryName));
    }

    @Transactional(readOnly = true)
    public List<MapCategoryResDto> getMapCategories(Long festivalId) {
        return findMapCategoriesByFestivalIdToDto(festivalId);
    }

    @Transactional(readOnly = true)
    public MapCategoryImageDto getIconAndPin(Long mapCategoryId) {
        MapCategory mapCategory = loadMapCategoryOrThrow(mapCategoryId);
        return MapCategoryImageDto.from(mapCategory);
    }

    @Transactional
    public void updateIconAndPin(Long mapCategoryId, MapCategoryImageDto mapCategoryImageDto) {
        MapCategory mapCategory = loadMapCategoryOrThrow(mapCategoryId);
        mapCategory.updateIconAndPin(mapCategoryImageDto);
    }

    @Transactional
    public void delete(Long categoryId) {
        mapCategoryRepository.deleteById(categoryId);
    }

    @Transactional
    public List<MapCategoryResDto> updateDisplayOrder(Long festivalId, List<OrderUpdateRequest> orderRequests) {
        List<MapCategory> mapCategories = mapCategoryRepository.findAllByFestivalId(festivalId);

        DisplayOrderUtils.updateDisplayOrder(mapCategories, orderRequests);

        return mapCategories.stream().map(MapCategoryResDto::from).collect(Collectors.toList());
    }

    private List<MapCategoryResDto> findMapCategoriesByFestivalIdToDto(Long festivalId) {
        return mapCategoryRepository.findAllByFestivalId(festivalId).stream()
                .map(MapCategoryResDto::from)
                .collect(Collectors.toList());
    }

    private MapCategory loadMapCategoryOrThrow(Long id) {
        return mapCategoryRepository.findById(id).orElseThrow(() -> new MapCategoryNotFoundException(id));
    }
}
