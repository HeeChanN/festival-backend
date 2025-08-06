package com.halo.eventer.global.security;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.member.Member;
import com.halo.eventer.global.config.security.*;
import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.TestController;
import com.halo.eventer.global.error.TestDto;
import com.halo.eventer.global.security.provider.JwtProvider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestController.class)
@AutoConfigureMockMvc
@Import({SecurityTestBeans.class, SecurityConfig.class})
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class SecurityConfigTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    JwtProvider jwtProvider;

    private TestDto testDto;

    @Nested
    class CORS_테스트 {

        @Test
        void CORS_거부_테스트() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test/festival")
                            .header("Origin", "http://not-allowed.com")
                            .header("Access-Control-Request-Method", "GET"))
                    .andExpect(status().isForbidden())
                    .andExpect(content().string("Invalid CORS request"));
        }

        @Test
        void CORS_허용_테스트() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/test/festival")
                            .header("Origin", "http://localhost:3000")
                            .header("Access-Control-Request-Method", "GET"))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                    .andExpect(content().string("festival"));
        }
    }

    @Nested
    class ExceptionHandling_테스트 {

        @BeforeEach
        void setUp() {
            testDto = new TestDto("이름", "비밀번호");
        }

        @Test
        void AuthenticationException_인증_예외처리_테스트() throws Exception {
            given(jwtProvider.resolveToken(any())).willReturn(null);

            mockMvc.perform(post("/test/festival")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testDto)))
                    .andExpect(status().is(401))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(ErrorCode.UN_AUTHENTICATED.getCode()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.UN_AUTHENTICATED.getMessage()))
                    .andExpect(jsonPath("$.status").value(ErrorCode.UN_AUTHENTICATED.getStatus()));
        }

        @Test
        void AccessDeniedException_인가_예외처리_테스트() throws Exception {
            String tokenWithPrefix = "Bearer valid.token.here";
            given(jwtProvider.resolveToken(any())).willReturn(tokenWithPrefix);
            given(jwtProvider.validateToken(tokenWithPrefix)).willReturn(true);
            CustomUserDetails customUserDetails = new CustomUserDetails(new Member("user", "1234"));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
            given(jwtProvider.getAuthentication("valid.token.here")).willReturn(auth);

            mockMvc.perform(post("/test/festival")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testDto)))
                    .andExpect(status().is(403))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(ErrorCode.UN_AUTHORIZED.getCode()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.UN_AUTHORIZED.getMessage()))
                    .andExpect(jsonPath("$.status").value(ErrorCode.UN_AUTHORIZED.getStatus()));
        }
    }
}
