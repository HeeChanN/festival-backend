package com.halo.eventer.domain.map.service;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.map.Menu;
import com.halo.eventer.domain.map.MenuFixture;
import com.halo.eventer.domain.map.dto.menu.MenuCreateDto;
import com.halo.eventer.domain.map.dto.menu.MenuResDto;
import com.halo.eventer.domain.map.dto.menu.MenuUpdateDto;
import com.halo.eventer.domain.map.repository.MenuJdbcRepository;
import com.halo.eventer.domain.map.repository.MenuRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuJdbcRepository menuJdbcRepository;

    @InjectMocks
    private MenuService menuService;

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
    void 메뉴_여러개_한번에_생성_테스트() {
        // given
        final long mapId = 1L;
        List<MenuCreateDto> menuCreateDtos = List.of(menuCreateDto);
        doNothing().when(menuJdbcRepository).batchInsertMenu(eq(mapId), any());

        // when
        menuService.create(menuCreateDtos, mapId);

        // then
        verify(menuJdbcRepository, times(1)).batchInsertMenu(eq(mapId), any());
    }

    @Test
    void 메뉴_리스트_조회_테스트() {
        // given
        final long mapId = 1L;
        given(menuRepository.findAllByMapId(mapId)).willReturn(List.of(menu));

        // when
        List<MenuResDto> result = menuService.getMenus(mapId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo(menu.getName());
        assertThat(result.get(0).getPrice()).isEqualTo(menu.getPrice());
        assertThat(result.get(0).getImage()).isEqualTo(menu.getImage());
        assertThat(result.get(0).getSummary()).isEqualTo(menu.getSummary());
    }

    @Test
    void 메뉴_수정_테스트() {
        // given
        List<MenuUpdateDto> menuUpdateDtos = List.of(menuUpdateDto);
        setField(menu, "id", 1L);
        given(menuRepository.findAllByIdIn(anyList())).willReturn(List.of(menu));
        given(menuRepository.findAllByMapId(1L)).willReturn(List.of(menu));

        // when
        List<MenuResDto> result = menuService.update(1L, menuUpdateDtos);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo(menu.getName());
        assertThat(result.get(0).getPrice()).isEqualTo(menu.getPrice());
        assertThat(result.get(0).getImage()).isEqualTo(menu.getImage());
        assertThat(result.get(0).getSummary()).isEqualTo(menu.getSummary());
    }

    @Test
    void 메뉴_삭제_테스트() {
        // given
        doNothing().when(menuRepository).deleteById(1L);

        // when
        menuService.delete(1L);

        // then
        verify(menuRepository, times(1)).deleteById(1L);
    }
}
