package com.halo.eventer.domain.image.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.image.api_docs.ImageDoc;
import com.halo.eventer.domain.image.service.ImageService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(ImageController.class)
@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ImageService imageService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Test
    void presigned_URL_발급() throws Exception {
        // given
        given(imageService.generatePreSignedUploadUrl(anyString())).willReturn("https://<버킷주소>/<objectKey>");

        // when & then
        mockMvc.perform(get("/upload/preSigned").queryParam("fileExtension", "jpg"))
                .andExpect(content().string("https://<버킷주소>/<objectKey>"))
                .andDo(ImageDoc.getPreSignedUploadUrl());
    }
}
