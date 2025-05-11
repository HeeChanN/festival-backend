package com.halo.eventer.domain.map.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.map.dto.menu.MenuCreateDto;
import com.halo.eventer.domain.map.dto.menu.MenuResDto;
import com.halo.eventer.domain.map.dto.menu.MenuUpdateDto;
import com.halo.eventer.domain.map.service.MenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "메뉴")
@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuController {

    private final MenuService menuService;

    @PostMapping()
    public void create(@RequestBody List<MenuCreateDto> menuCreateDtos, @RequestParam("mapId") Long id) {
        menuService.create(menuCreateDtos, id);
    }

    @GetMapping()
    public List<MenuResDto> getMenus(@RequestParam("mapId") Long id) {
        return menuService.getMenus(id);
    }

    @PatchMapping()
    public List<MenuResDto> update(@RequestParam("mapId") Long id, @RequestBody List<MenuUpdateDto> menuUpdateDtos) {
        return menuService.update(id, menuUpdateDtos);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        menuService.delete(id);
    }
}
