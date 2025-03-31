package com.halo.eventer.global.config.security;

import com.halo.eventer.global.constants.SecurityConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class AuthorizationConfig {

    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(SecurityConstants.SWAGGER_URLS).permitAll()
                .antMatchers(HttpMethod.GET, SecurityConstants.PUBLIC_GET_URLS).permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.PUBLIC_POST_URLS).permitAll()
                .antMatchers(HttpMethod.PATCH, SecurityConstants.PUBLIC_PATCH_URLS).permitAll()
                .antMatchers(HttpMethod.GET, SecurityConstants.ACTUATOR_URL).permitAll() // 내부 모니터링 + 포트번호로 제어
                .anyRequest().hasRole("ADMIN");
    }
}