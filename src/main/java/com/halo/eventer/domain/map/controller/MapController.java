package com.halo.eventer.domain.map.controller;



import com.halo.eventer.domain.map.dto.map.MapItemDto;
import com.halo.eventer.domain.map.dto.map.MapCreateDto;
import com.halo.eventer.domain.map.dto.map.MapResDto;
import com.halo.eventer.domain.map.dto.map.MapUpdateDto;
import com.halo.eventer.domain.map.service.MapService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "지도")
@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {

    private final MapService mapService;

    @PostMapping()
    public MapResDto createMap(@RequestBody MapCreateDto mapCreateDto,
                               @RequestParam("mapCategoryId") Long mapCategoryId){
        return mapService.create(mapCreateDto,mapCategoryId);
    }

    @GetMapping("/{mapId}")
    public MapResDto getMap(@PathVariable("mapId")Long mapId){
        return mapService.getMap(mapId);
    }

    @GetMapping()
    public List<MapItemDto> getMaps(@RequestParam("mapCategoryId") Long mapCategoryId){
        return mapService.getMaps(mapCategoryId);
    }

    @PutMapping("/{mapId}")
    public MapResDto update(@PathVariable("mapId") Long mapId,
                               @RequestBody MapUpdateDto mapUpdateDto,
                               @RequestParam("mapCategoryId") Long mapCategoryId){
        return mapService.update(mapId, mapUpdateDto, mapCategoryId);
    }

    @DeleteMapping("/{mapId}")
    public void delete(@PathVariable("mapId") Long id){
        mapService.delete(id);
    }
}
