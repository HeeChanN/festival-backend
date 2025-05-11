package com.halo.eventer.global.error;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class GlobalExceptionHandlerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void 잘못된_http_메서드_호출_했을경우() throws Exception {
        this.mockMvc
                .perform(put("/api/test/method").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("message").value(ErrorCode.METHOD_NOT_ALLOWED.getMessage()))
                .andExpect(jsonPath("code").value(ErrorCode.METHOD_NOT_ALLOWED.getCode()));
    }

    @Test
    void JSON_요청본문이_잘못된_경우() throws Exception {
        this.mockMvc
                .perform(post("/api/test/requestBody").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(ErrorCode.INVALID_JSON_FORMAT.getMessage()))
                .andExpect(jsonPath("code").value(ErrorCode.INVALID_JSON_FORMAT.getCode()));
    }

    @Test
    void 필수_Parameter_누락된_경우() throws Exception {
        this.mockMvc
                .perform(get("/api/test/requestParam").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(ErrorCode.MISSING_PARAMETER.getMessage()))
                .andExpect(jsonPath("code").value(ErrorCode.MISSING_PARAMETER.getCode()));
    }

    @Test
    void Parameter_타입이_불일치하는_경우() throws Exception {
        this.mockMvc
                .perform(get("/api/test/requestParam?param=string").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value(ErrorCode.INVALID_PARAMETER_TYPE.getMessage()))
                .andExpect(jsonPath("code").value(ErrorCode.INVALID_PARAMETER_TYPE.getCode()));
    }
}
