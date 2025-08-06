package com.halo.eventer.domain.map.controller;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.map.dto.menu.MenuCreateDto;
import com.halo.eventer.domain.map.dto.menu.MenuResDto;
import com.halo.eventer.domain.map.dto.menu.MenuUpdateDto;
import com.halo.eventer.domain.map.service.MenuService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/maps/{mapId}/menus")
    public void create(
            @Min(1) @PathVariable("mapId") Long mapId, @Valid @RequestBody List<MenuCreateDto> menuCreateDtos) {
        menuService.create(menuCreateDtos, mapId);
    }

    @GetMapping("/maps/{mapId}/menus")
    public List<MenuResDto> getMenus(@Min(1) @PathVariable("mapId") Long mapId) {
        return menuService.getMenus(mapId);
    }

    @PatchMapping("/maps/{mapId}/menus")
    public List<MenuResDto> update(
            @Min(1) @PathVariable("mapId") Long mapId, @Valid @RequestBody List<MenuUpdateDto> menuUpdateDtos) {
        return menuService.update(mapId, menuUpdateDtos);
    }

    @DeleteMapping("/menus/{id}")
    public void delete(@Min(1) @PathVariable("id") Long id) {
        menuService.delete(id);
    }
}
