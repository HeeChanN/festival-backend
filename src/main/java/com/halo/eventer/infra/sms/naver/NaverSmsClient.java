package com.halo.eventer.infra.sms.naver;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.BaseException;
import com.halo.eventer.infra.sms.SmsClient;
import com.halo.eventer.infra.sms.common.SmsSendRequest;
import com.halo.eventer.infra.sms.naver.dto.NaverSmsResDto;
import com.halo.eventer.infra.sms.naver.util.NaverRequest;
import com.halo.eventer.infra.sms.naver.util.NaverUrlBuilder;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NaverSmsClient implements SmsClient {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud-sms.senderPhone}")
    private String senderPhone;

    @Value("${naver-cloud-sms.api-base-url}")
    private String apiBaseUrl;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    public NaverSmsClient(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public NaverSmsResDto sendToMany(List<SmsSendRequest> smsSendRequests) {

        String endpoint = NaverUrlBuilder.buildMessageEndpoint(serviceId);
        String url = apiBaseUrl + endpoint;

        HttpEntity<String> httpEntity = NaverRequest.builder(objectMapper, accessKey, secretKey, senderPhone)
                .header(endpoint)
                .body(smsSendRequests)
                .build();

        try {
            return restTemplate.postForObject(url, httpEntity, NaverSmsResDto.class);
        } catch (Exception e) {
            throw new BaseException(e.getMessage(), ErrorCode.SMS_SEND_FAILED);
        }
    }
}
