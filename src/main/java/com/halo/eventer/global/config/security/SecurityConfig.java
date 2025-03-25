package com.halo.eventer.global.config.security;

import com.halo.eventer.global.security.exception.CustomAccessDeniedHandler;
import com.halo.eventer.global.security.exception.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final CorsConfigurationSource corsConfig;
  private final AuthorizationConfig authorizationConfig;
  private final JwtAuthenticationFilterConfig jwtAuthenticationFilterConfig;

  public SecurityConfig(@Qualifier("customCorsConfigurationSource")CorsConfigurationSource corsConfig,
                        AuthorizationConfig authorizationConfig,
                        JwtAuthenticationFilterConfig jwtAuthenticationFilterConfig) {
    this.corsConfig = corsConfig;
    this.authorizationConfig = authorizationConfig;
    this.jwtAuthenticationFilterConfig = jwtAuthenticationFilterConfig;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .cors().configurationSource(corsConfig)
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    authorizationConfig.configure(http);
    jwtAuthenticationFilterConfig.configure(http);

    http.exceptionHandling()
            .accessDeniedHandler(new CustomAccessDeniedHandler())
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

    return http.build();
  }
}
