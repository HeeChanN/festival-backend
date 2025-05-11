package com.halo.eventer.global.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.halo.eventer.global.security.filter.JwtAuthenticationFilter;
import com.halo.eventer.global.security.filter.SecurityExceptionFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityExceptionFilterConfig {

    private final HandlerExceptionResolver handlerExceptionResolver;

    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new SecurityExceptionFilter(handlerExceptionResolver), JwtAuthenticationFilter.class);
    }
}
