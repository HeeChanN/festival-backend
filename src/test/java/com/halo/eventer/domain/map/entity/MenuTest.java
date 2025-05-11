package com.halo.eventer.domain.map.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.map.Menu;
import com.halo.eventer.domain.map.MenuFixture;
import com.halo.eventer.domain.map.dto.menu.MenuCreateDto;
import com.halo.eventer.domain.map.dto.menu.MenuUpdateDto;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class MenuTest {

    private MenuCreateDto menuCreateDto;
    private MenuUpdateDto menuUpdateDto;
    private Menu menu;

    @BeforeEach
    public void setUp() {
        menuCreateDto = MenuFixture.메뉴_생성_DTO();
        menuUpdateDto = MenuFixture.메뉴_수정_DTO();
        menu = MenuFixture.메뉴_엔티티(menuCreateDto);
    }

    @Test
    void 메뉴_생성_테스트() {
        // when
        Menu result = new Menu(menuCreateDto);

        // then
        assertThat(result.getSummary()).isEqualTo(menuCreateDto.getSummary());
        assertThat(result.getName()).isEqualTo(menuCreateDto.getName());
        assertThat(result.getPrice()).isEqualTo(menuCreateDto.getPrice());
        assertThat(result.getImage()).isEqualTo(menuCreateDto.getImage());
    }

    @Test
    void 메뉴_수정_테스트() {
        // when
        menu.updateMenu(menuUpdateDto);

        // then
        assertThat(menu.getSummary()).isEqualTo(menuUpdateDto.getSummary());
        assertThat(menu.getName()).isEqualTo(menuUpdateDto.getName());
        assertThat(menu.getPrice()).isEqualTo(menuUpdateDto.getPrice());
        assertThat(menu.getImage()).isEqualTo(menuUpdateDto.getImage());
    }
}
