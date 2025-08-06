package com.halo.eventer.domain.map.controller;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryCreateDto;
import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryImageDto;
import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryResDto;
import com.halo.eventer.domain.map.service.MapCategoryService;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MapCategoryController {

    private final MapCategoryService mapCategoryService;

    @PostMapping("/{festivalId}/map-categories")
    public void create(
            @Min(1) @PathVariable("festivalId") Long festivalId,
            @Valid @RequestBody MapCategoryCreateDto mapCategoryCreateDto) {
        mapCategoryService.create(festivalId, mapCategoryCreateDto.getName());
    }

    @GetMapping("/map-categories/{mapCategoryId}/image")
    public MapCategoryImageDto getMapCategoryImages(@Min(1) @PathVariable("mapCategoryId") Long mapCategoryId) {
        return mapCategoryService.getIconAndPin(mapCategoryId);
    }

    @GetMapping("/{festivalId}/map-categories")
    public List<MapCategoryResDto> getMapCategories(@Min(1) @PathVariable("festivalId") Long festivalId) {
        return mapCategoryService.getMapCategories(festivalId);
    }

    @PatchMapping("/map-categories/{mapCategoryId}/image")
    public void updateIconAndPin(
            @Min(1) @PathVariable("mapCategoryId") Long mapCategoryId,
            @RequestBody MapCategoryImageDto mapCategoryImageDto) {
        mapCategoryService.updateIconAndPin(mapCategoryId, mapCategoryImageDto);
    }

    @PatchMapping("/{festivalId}/map-categories/displayOrder")
    public void updateDisplayOrder(
            @Min(1) @PathVariable("festivalId") Long festivalId,
            @Valid @RequestBody List<OrderUpdateRequest> orderUpdateRequests) {
        mapCategoryService.updateDisplayOrder(festivalId, orderUpdateRequests);
    }

    @DeleteMapping("/map-categories/{mapCategoryId}")
    public void delete(@Min(1) @PathVariable("mapCategoryId") Long mapCategoryId) {
        mapCategoryService.delete(mapCategoryId);
    }
}
