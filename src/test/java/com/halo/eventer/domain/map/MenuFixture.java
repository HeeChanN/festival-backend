package com.halo.eventer.domain.map;

import com.halo.eventer.domain.map.dto.menu.MenuCreateDto;
import com.halo.eventer.domain.map.dto.menu.MenuUpdateDto;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu 메뉴_엔티티(MenuCreateDto dto) {
        return new Menu(dto);
    }

    public static MenuCreateDto 메뉴_생성_DTO() {
        MenuCreateDto menuCreateDto = new MenuCreateDto();
        setField(menuCreateDto, "name", "name");
        setField(menuCreateDto, "price", 100);
        setField(menuCreateDto, "summary", "summary");
        setField(menuCreateDto, "image", "image");
        return menuCreateDto;
    }

    public static MenuUpdateDto 메뉴_수정_DTO() {
        MenuUpdateDto menuUpdateDto = new MenuUpdateDto();
        setField(menuUpdateDto, "id", 1L);
        setField(menuUpdateDto, "name", "u_name");
        setField(menuUpdateDto, "price", 1000);
        setField(menuUpdateDto, "summary", "u_summary");
        setField(menuUpdateDto, "image", "u_image");
        return menuUpdateDto;
    }
}
