package com.halo.eventer.domain.notice.controller;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.notice.ArticleType;
import com.halo.eventer.domain.notice.NoticeFixture;
import com.halo.eventer.domain.notice.controller.user.NoticeController;
import com.halo.eventer.domain.notice.dto.NoticeCreateReqDto;
import com.halo.eventer.domain.notice.dto.user.UserNoticeNoOffsetPageDto;
import com.halo.eventer.domain.notice.dto.user.UserNoticeResDto;
import com.halo.eventer.domain.notice.service.NoticeService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoticeController.class)
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class NoticeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoticeService noticeService;

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    void 유저용_notice_단일조회_성공() throws Exception {
        NoticeCreateReqDto noticeCreateReqDto = NoticeFixture.공지사항_생성_DTO();
        UserNoticeResDto response = UserNoticeResDto.builder()
                .title(noticeCreateReqDto.getTitle())
                .content(noticeCreateReqDto.getContent())
                .build();
        given(noticeService.getNoticeByIdForUser(eq(1L))).willReturn(response);

        mockMvc.perform(get("/notices/{noticesId}", 1L).param("type", "NOTICE").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(noticeCreateReqDto.getTitle()))
                .andExpect(jsonPath("$.content").value(noticeCreateReqDto.getContent()));
    }

    @Test
    void 파라미터_생략시_DTO_기본값으로_서비스_호출() throws Exception {
        // given – 서비스 Mock 반환값
        UserNoticeNoOffsetPageDto noticeNoOffsetPageDto = new UserNoticeNoOffsetPageDto();
        given(noticeService.getNoticesByTypeWithNoOffsetPaging(
                        1L, ArticleType.NOTICE, LocalDateTime.of(9999, 12, 31, 23, 59, 59), 0L, 20))
                .willReturn(noticeNoOffsetPageDto);

        // when
        mockMvc.perform(get("/notices")
                        .param("festivalId", "1")
                        .param("type", "NOTICE")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then – 서비스가 기대 값으로 호출됐는지 검증
        then(noticeService)
                .should()
                .getNoticesByTypeWithNoOffsetPaging(
                        1L, ArticleType.NOTICE, LocalDateTime.of(9999, 12, 31, 23, 59, 59), 0L, 20);
    }
}
