package com.halo.eventer.infra.sms.naver.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.BaseException;
import com.halo.eventer.infra.sms.common.SmsSendRequest;
import com.halo.eventer.infra.sms.naver.dto.NaverMessageReqDto;
import com.halo.eventer.infra.sms.naver.dto.NaverSmsReqDto;

public class NaverRequest {

    private final ObjectMapper objectMapper;
    private final String accessKey;
    private final String secretKey;
    private final String senderPhone;

    private String defaultMessage;
    private HttpHeaders headers;
    private List<NaverMessageReqDto> messageReqDtos = new ArrayList<>();

    private NaverRequest(ObjectMapper objectMapper, String accessKey, String secretKey, String senderPhone) {
        this.objectMapper = objectMapper;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.senderPhone = senderPhone;
    }

    public static NaverRequest builder(
            ObjectMapper objectMapper, String accessKey, String secretKey, String senderPhone) {
        return new NaverRequest(objectMapper, accessKey, secretKey, senderPhone);
    }

    public NaverRequest header(String endpoint) {
        try {
            this.headers = createHeaders(endpoint);
        } catch (Exception e) {
            throw new BaseException("Failed to build SMS body", ErrorCode.SMS_SEND_FAILED);
        }
        return this;
    }

    public NaverRequest body(List<SmsSendRequest> smsSendRequests) {
        this.defaultMessage = "Default message";
        smsSendRequests.forEach(smsSendRequest -> {
            NaverMessageReqDto messageReqDto = NaverMessageReqDto.builder()
                    .to(smsSendRequest.getTo())
                    .content(smsSendRequest.getMessage())
                    .build();
            messageReqDtos.add(messageReqDto);
        });
        return this;
    }

    public HttpEntity<String> build() {
        NaverSmsReqDto naverSmsReqDto = NaverSmsReqDto.builder()
                .type("LMS")
                .contentType("COMM")
                .countryCode("82")
                .from(senderPhone)
                .content(defaultMessage)
                .messages(messageReqDtos)
                .build();

        try {
            String bodyStr = objectMapper.writeValueAsString(naverSmsReqDto);
            return new HttpEntity<>(bodyStr, headers);
        } catch (JsonProcessingException e) {
            throw new BaseException("Failed to build SMS message", ErrorCode.SMS_SEND_FAILED);
        }
    }

    private HttpHeaders createHeaders(String endpoint)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        final long currentTime = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", String.valueOf(currentTime));
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(currentTime, endpoint));
        return headers;
    }

    private String makeSignature(long time, String endpoint)
            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String timestamp = String.valueOf(time);

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(endpoint)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        return Base64.encodeBase64String(rawHmac);
    }
}
