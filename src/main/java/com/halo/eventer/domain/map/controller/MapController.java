package com.halo.eventer.domain.map.controller;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.map.dto.map.MapCreateDto;
import com.halo.eventer.domain.map.dto.map.MapItemDto;
import com.halo.eventer.domain.map.dto.map.MapResDto;
import com.halo.eventer.domain.map.dto.map.MapUpdateDto;
import com.halo.eventer.domain.map.service.MapService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @PostMapping("/map-categories/{mapCategoryId}/maps")
    public MapResDto createMap(
            @Min(1) @PathVariable("mapCategoryId") Long mapCategoryId, @Valid @RequestBody MapCreateDto mapCreateDto) {
        return mapService.create(mapCreateDto, mapCategoryId);
    }

    @GetMapping("/maps/{mapId}")
    public MapResDto getMap(@Min(1) @PathVariable("mapId") Long mapId) {
        return mapService.getMap(mapId);
    }

    @GetMapping("/map-categories/{mapCategoryId}/maps")
    public List<MapItemDto> getMaps(@Min(1) @PathVariable("mapCategoryId") Long mapCategoryId) {
        return mapService.getMaps(mapCategoryId);
    }

    @PutMapping("/map-categories/{mapCategoryId}/maps/{mapId}")
    public MapResDto update(
            @Min(1) @PathVariable("mapCategoryId") Long mapCategoryId,
            @Min(1) @PathVariable("mapId") Long mapId,
            @Valid @RequestBody MapUpdateDto mapUpdateDto) {
        return mapService.update(mapId, mapUpdateDto, mapCategoryId);
    }

    @DeleteMapping("/maps/{mapId}")
    public void delete(@Min(1) @PathVariable("mapId") Long id) {
        mapService.delete(id);
    }
}
