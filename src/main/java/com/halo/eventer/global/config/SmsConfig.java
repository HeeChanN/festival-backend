package com.halo.eventer.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SmsConfig {

    @Bean(name = "simpleRestTemplate")
    public RestTemplate smsRestTemplate() {
        return new RestTemplate();
    }
}
