package com.halo.eventer.global.common;

import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ApiErrorAssert {

    private ApiErrorAssert() {}

    public static ResultActions assertError(
            ResultActions actions, String expectedCode, String expectedMessage, int expectedBodyStatus)
            throws Exception {
        return actions.andExpect(jsonPath("$.code").value(expectedCode))
                .andExpect(jsonPath("$.status").value(expectedBodyStatus))
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }
}
