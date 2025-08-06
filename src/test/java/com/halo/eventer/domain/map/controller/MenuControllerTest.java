package com.halo.eventer.domain.map.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.map.MenuFixture;
import com.halo.eventer.domain.map.api_docs.MenuDoc;
import com.halo.eventer.domain.map.dto.menu.MenuCreateDto;
import com.halo.eventer.domain.map.dto.menu.MenuResDto;
import com.halo.eventer.domain.map.dto.menu.MenuUpdateDto;
import com.halo.eventer.domain.map.service.MenuService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static com.halo.eventer.global.common.ApiErrorAssert.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuController.class)
@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MenuService menuService;

    @MockitoBean
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
        mockMvc.perform(post("/maps/{mapId}/menus", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuCreateDtos)))
                .andExpect(status().isOk())
                .andDo(MenuDoc.createMenus());
        verify(menuService, times(1)).create(anyList(), anyLong());
    }

    @Test
    void 메뉴_생성_테스트_인증거부() throws Exception {
        // given
        List<MenuCreateDto> menuCreateDtos = List.of(menuCreateDto);

        // when & then
        ResultActions result = mockMvc.perform(post("/maps/{mapId}/menus", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuCreateDtos)))
                .andExpect(status().isUnauthorized())
                .andDo(MenuDoc.errorSnippet("메뉴_생성_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 메뉴_생성시_메뉴이름이_20자가_넘어갈_경우() throws Exception {
        // given
        MenuCreateDto errorRequest = new MenuCreateDto("abcdefghijklmnopqrstuv", 1000, "summary", "image");
        List<MenuCreateDto> menuCreateDtos = List.of(errorRequest);

        // when & then
        ResultActions result = mockMvc.perform(post("/maps/{mapId}/menus", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuCreateDtos)))
                .andExpect(status().isBadRequest())
                .andDo(MenuDoc.errorSnippet("메뉴_생성_이름_길이_초과"));
        assertError(result, "C013", "size must be between 1 and 20", 400);
    }

    @Test
    void 메뉴_리스트_조회_테스트() throws Exception {
        // given
        MenuResDto menuResDto = new MenuResDto(1L, "메뉴 이름", 1000, "메뉴 이미지", "메뉴 설명");
        List<MenuResDto> menuResDtos = List.of(menuResDto);
        given(menuService.getMenus(anyLong())).willReturn(menuResDtos);

        // when & then
        mockMvc.perform(get("/maps/{mapId}/menus", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").value(menuResDto.getId()))
                .andExpect(jsonPath("$.[0].name").value(menuResDto.getName()))
                .andExpect(jsonPath("$.[0].price").value(menuResDto.getPrice()))
                .andExpect(jsonPath("$.[0].image").value(menuResDto.getImage()))
                .andExpect(jsonPath("$.[0].summary").value(menuResDto.getSummary()))
                .andDo(MenuDoc.getMenus());
        verify(menuService, times(1)).getMenus(anyLong());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 메뉴_수정_테스트() throws Exception {
        // given
        MenuUpdateDto menuUpdateDto = MenuFixture.메뉴_수정_DTO();
        List<MenuUpdateDto> menuUpdateDtos = List.of(menuUpdateDto);
        MenuResDto menuResDto = new MenuResDto(
                menuUpdateDto.getId(),
                menuUpdateDto.getName(),
                menuUpdateDto.getPrice(),
                menuUpdateDto.getImage(),
                menuUpdateDto.getSummary());
        List<MenuResDto> menuResDtos = List.of(menuResDto);
        given(menuService.update(anyLong(), anyList())).willReturn(menuResDtos);

        // when & then
        mockMvc.perform(patch("/maps/{mapId}/menus", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuUpdateDtos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].id").value(menuResDto.getId()))
                .andExpect(jsonPath("$.[0].name").value(menuResDto.getName()))
                .andExpect(jsonPath("$.[0].price").value(menuResDto.getPrice()))
                .andExpect(jsonPath("$.[0].image").value(menuResDto.getImage()))
                .andExpect(jsonPath("$.[0].summary").value(menuResDto.getSummary()))
                .andDo(MenuDoc.updateMenus());
        verify(menuService, times(1)).update(anyLong(), anyList());
    }

    @Test
    void 메뉴_수정_인증거부() throws Exception {
        // given
        MenuUpdateDto menuUpdateDto = MenuFixture.메뉴_수정_DTO();
        List<MenuUpdateDto> menuUpdateDtos = List.of(menuUpdateDto);

        // when & then
        ResultActions result = mockMvc.perform(patch("/maps/{mapId}/menus", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuUpdateDtos)))
                .andExpect(status().isUnauthorized())
                .andDo(MenuDoc.errorSnippet("메뉴_수정_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 메뉴_수정시_메뉴이름이_20자_넘어갈_경우() throws Exception {
        // given
        MenuUpdateDto menuUpdateDto = new MenuUpdateDto(1L, "abcdefghijklmnopqrstuv", 1000, "summary", "image");
        List<MenuUpdateDto> menuUpdateDtos = List.of(menuUpdateDto);

        // when & then
        ResultActions result = mockMvc.perform(patch("/maps/{mapId}/menus", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuUpdateDtos)))
                .andExpect(status().isBadRequest())
                .andDo(MenuDoc.errorSnippet("메뉴_수정_이름_길이_초과"));
        assertError(result, "C013", "size must be between 1 and 20", 400);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 메뉴_삭제_테스트() throws Exception {
        // given
        doNothing().when(menuService).delete(anyLong());

        // when & then
        mockMvc.perform(delete("/menus/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MenuDoc.deleteMenu());
        verify(menuService, times(1)).delete(anyLong());
    }

    @Test
    void 메뉴_삭제_인증거부() throws Exception {
        // when & then
        ResultActions result = mockMvc.perform(delete("/menus/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(MenuDoc.errorSnippet("메뉴_삭제_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }
}
