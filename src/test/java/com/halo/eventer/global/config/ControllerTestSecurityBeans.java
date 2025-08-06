package com.halo.eventer.global.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.halo.eventer.global.config.security.AuthorizationConfig;
import com.halo.eventer.global.config.security.JwtAuthenticationFilterConfig;
import com.halo.eventer.global.config.security.SecurityExceptionFilterConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

@TestConfiguration
public class ControllerTestSecurityBeans {

    @Bean(name = "customCorsConfigurationSource")
    CorsConfigurationSource corsConfigurationSource() {
        return Mockito.mock(CorsConfigurationSource.class);
    }

    @Bean
    public JwtAuthenticationFilterConfig jwtAuthenticationFilterConfig(JwtProvider jwtProvider) {
        return new JwtAuthenticationFilterConfig(jwtProvider);
    }

    @Bean
    public AuthorizationConfig authorizationConfig() {
        return new AuthorizationConfig();
    }

    @Bean
    public SecurityExceptionFilterConfig securityExceptionFilterConfig(
            HandlerExceptionResolver handlerExceptionResolver) {
        return new SecurityExceptionFilterConfig(handlerExceptionResolver);
    }
}
