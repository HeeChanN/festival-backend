package com.halo.eventer.global.security;

import com.halo.eventer.global.config.security.AuthorizationConfig;
import com.halo.eventer.global.config.security.CorsConfig;
import com.halo.eventer.global.config.security.JwtAuthenticationFilterConfig;
import com.halo.eventer.global.config.security.SecurityExceptionFilterConfig;
import com.halo.eventer.global.security.provider.JwtProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

@TestConfiguration
public class SecurityTestBeans {

    @Bean
    @Qualifier("customCorsConfigurationSource")
    public CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfig().customCorsConfigurationSource();
    }

    @Bean
    public AuthorizationConfig authorizationConfig() {
        return new AuthorizationConfig();
    }

    @Bean
    public JwtAuthenticationFilterConfig jwtAuthenticationFilterConfig(JwtProvider jwtProvider) {
        return new JwtAuthenticationFilterConfig(jwtProvider);
    }

    @Bean
    public SecurityExceptionFilterConfig securityExceptionFilterConfig(HandlerExceptionResolver handlerExceptionResolver) {
        return new SecurityExceptionFilterConfig(handlerExceptionResolver);
    }
}
