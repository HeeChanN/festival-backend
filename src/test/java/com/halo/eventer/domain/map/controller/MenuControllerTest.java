package com.halo.eventer.domain.map.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.map.MenuFixture;
import com.halo.eventer.domain.map.dto.menu.MenuCreateDto;
import com.halo.eventer.domain.map.dto.menu.MenuResDto;
import com.halo.eventer.domain.map.dto.menu.MenuUpdateDto;
import com.halo.eventer.domain.map.service.MenuService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuController.class)
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @MockBean
    private JwtProvider jwtProvider;

    private MenuCreateDto menuCreateDto;

    @BeforeEach
    public void setUp() {
        menuCreateDto = MenuFixture.메뉴_생성_DTO();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 메뉴_생성_테스트() throws Exception {
        // given
        doNothing().when(menuService).create(anyList(), anyLong());
        List<MenuCreateDto> menuCreateDtos = List.of(menuCreateDto);

        // when & then
        mockMvc.perform(post("/menus")
                        .param("mapId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuCreateDtos)))
                .andExpect(status().isOk());
        verify(menuService, times(1)).create(anyList(), anyLong());
    }

    @Test
    void 메뉴_생성_테스트_권한예외() throws Exception {
        // given
        List<MenuCreateDto> menuCreateDtos = List.of(menuCreateDto);

        // when & then
        mockMvc.perform(post("/menus")
                        .param("mapId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuCreateDtos)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 메뉴_리스트_조회_테스트() throws Exception {
        // given
        MenuResDto menuResDto = new MenuResDto();
        List<MenuResDto> menuResDtos = List.of(menuResDto);
        given(menuService.getMenus(anyLong())).willReturn(menuResDtos);

        // when & then
        mockMvc.perform(get("/menus").param("mapId", "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(menuService, times(1)).getMenus(anyLong());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 메뉴_수정_테스트() throws Exception {
        // given
        MenuUpdateDto menuUpdateDto = MenuFixture.메뉴_수정_DTO();
        List<MenuUpdateDto> menuUpdateDtos = List.of(menuUpdateDto);
        MenuResDto menuResDto = new MenuResDto();
        List<MenuResDto> menuResDtos = List.of(menuResDto);
        given(menuService.update(anyLong(), anyList())).willReturn(menuResDtos);

        // when & then
        mockMvc.perform(patch("/menus")
                        .param("mapId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuUpdateDtos)))
                .andExpect(status().isOk());
        verify(menuService, times(1)).update(anyLong(), anyList());
    }

    @Test
    void 메뉴_수정_권한예외() throws Exception {
        // given
        MenuUpdateDto menuUpdateDto = MenuFixture.메뉴_수정_DTO();
        List<MenuUpdateDto> menuUpdateDtos = List.of(menuUpdateDto);

        // when & then
        mockMvc.perform(patch("/menus")
                        .param("mapId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuUpdateDtos)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 메뉴_삭제_테스트() throws Exception {
        // given
        doNothing().when(menuService).delete(anyLong());

        // when & then
        mockMvc.perform(delete("/menus/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(menuService, times(1)).delete(anyLong());
    }

    @Test
    void 메뉴_삭제_권한예외() throws Exception {
        // when & then
        mockMvc.perform(delete("/menus/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
