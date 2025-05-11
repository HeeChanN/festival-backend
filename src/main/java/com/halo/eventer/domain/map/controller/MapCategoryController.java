package com.halo.eventer.domain.map.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryImageDto;
import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryResDto;
import com.halo.eventer.domain.map.service.MapCategoryService;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mapCategory")
@Tag(name = "맵 카테고리")
public class MapCategoryController {

    private final MapCategoryService mapCategoryService;

    @PostMapping("/{festivalId}")
    public List<MapCategoryResDto> create(
            @PathVariable("festivalId") Long festivalId, @RequestParam("categoryName") String categoryName) {
        return mapCategoryService.create(festivalId, categoryName);
    }

    @GetMapping("/image")
    public MapCategoryImageDto getMapCategoryImages(@RequestParam("mapCategoryId") Long mapCategoryId) {
        return mapCategoryService.getIconAndPin(mapCategoryId);
    }

    @PatchMapping("/image/{mapCategoryId}")
    public void updateIconAndPin(
            @PathVariable("mapCategoryId") Long mapCategoryId, @RequestBody MapCategoryImageDto mapCategoryImageDto) {
        mapCategoryService.updateIconAndPin(mapCategoryId, mapCategoryImageDto);
    }

    @GetMapping
    public List<MapCategoryResDto> getMapCategories(@RequestParam("festivalId") Long festivalId) {
        return mapCategoryService.getMapCategories(festivalId);
    }

    @DeleteMapping("/{mapCategoryId}")
    public List<MapCategoryResDto> delete(
            @PathVariable("mapCategoryId") Long mapCategoryId, @RequestParam("festivalId") Long festivalId) {
        return mapCategoryService.delete(mapCategoryId, festivalId);
    }

    @PatchMapping("/displayOrder")
    public List<MapCategoryResDto> updateDisplayOrder(
            @RequestParam("festivalId") Long festivalId, @RequestBody List<OrderUpdateRequest> orderUpdateRequests) {
        return mapCategoryService.updateDisplayOrder(festivalId, orderUpdateRequests);
    }
}
