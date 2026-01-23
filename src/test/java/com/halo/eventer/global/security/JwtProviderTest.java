package com.halo.eventer.global.security;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.halo.eventer.global.security.provider.JwtProvider;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("deploy")
@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class JwtProviderTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 토큰_전달_테스트() throws Exception {
        String token = jwtProvider.createToken("admin", List.of("ADMIN"));

        mockMvc.perform(get("/").header("Authorization", "Bearer " + token)).andExpect(status().isOk());
    }

    @Test
    void STAMP_토큰_전달_테스트() throws Exception {
        String token = jwtProvider.createToken("uuid", List.of("STAMP"));

        mockMvc.perform(get("/").header("Authorization", "Bearer " + token)).andExpect(status().isOk());
    }
}
