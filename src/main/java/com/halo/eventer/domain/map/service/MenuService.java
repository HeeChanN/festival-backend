package com.halo.eventer.domain.map.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.map.Menu;
import com.halo.eventer.domain.map.dto.menu.MenuCreateDto;
import com.halo.eventer.domain.map.dto.menu.MenuResDto;
import com.halo.eventer.domain.map.dto.menu.MenuUpdateDto;
import com.halo.eventer.domain.map.repository.MenuJdbcRepository;
import com.halo.eventer.domain.map.repository.MenuRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuJdbcRepository menuJdbcRepository;

    @Transactional
    public void create(List<MenuCreateDto> menuCreateDtos, Long mapId) {
        menuJdbcRepository.batchInsertMenu(mapId, menuCreateDtos);
    }

    @Transactional(readOnly = true)
    public List<MenuResDto> getMenus(Long mapId) {
        return menuRepository.findAllByMapId(mapId).stream()
                .map(MenuResDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MenuResDto> update(Long mapId, List<MenuUpdateDto> menuUpdateDtos) {
        List<Menu> menus = menuRepository.findAllByIdIn(
                menuUpdateDtos.stream().map(MenuUpdateDto::getId).collect(Collectors.toList()));
        java.util.Map<Long, Menu> menuMap = menus.stream().collect(Collectors.toMap(Menu::getId, Function.identity()));
        menuUpdateDtos.forEach(menuUpdateDto -> {
            menuMap.get(menuUpdateDto.getId()).updateMenu(menuUpdateDto);
        });

        return menuRepository.findAllByMapId(mapId).stream()
                .map(MenuResDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        menuRepository.deleteById(id);
    }
}
