package com.halo.eventer.infra.naver.sms.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.InfraException;
import com.halo.eventer.infra.naver.sms.dto.*;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class SmsRequest {

  private final ObjectMapper objectMapper;

  @Value("${naver-cloud-sms.accessKey}")
  private String accessKey;

  @Value("${naver-cloud-sms.secretKey}")
  private String secretKey;

  @Value("${naver-cloud-sms.api-base-url}")
  private String apiBaseUrl;

  @Value("${naver-cloud-sms.senderPhone}")
  private String phone;

  public String makeSignature(Long time, String endpoint)
      throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
    String space = " ";
    String newLine = "\n";
    String method = "POST";
    String timestamp = time.toString();
    String accessKey = this.accessKey;
    String secretKey = this.secretKey;

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

  public HttpHeaders getSmsHeader(String endpoint)
          throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
    Long time = System.currentTimeMillis();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-ncp-apigw-timestamp", time.toString());
    headers.set("x-ncp-iam-access-key", accessKey);
    headers.set("x-ncp-apigw-signature-v2", makeSignature(time, endpoint));

    return headers;
  }

  public SmsReqDto getSmsBody(List<MessageDto> messages, String defaultMessage) {
    return SmsReqDto.builder()
        .type("LMS")
        .contentType("COMM")
        .countryCode("82")
        .from(phone)
        .content(defaultMessage)
        .messages(messages)
        .build();
  }

  public SmsResDto sendSmsReq(SmsReqDto smsReqDto, String endpoint) {
    try {
      // 요청 만들기
      String body = objectMapper.writeValueAsString(smsReqDto);
      HttpEntity<String> httpBody = new HttpEntity<>(body, getSmsHeader(endpoint));

      // 요청 보내기
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
      return restTemplate.postForObject(new URI(apiBaseUrl + endpoint), httpBody, SmsResDto.class);

    } catch (Exception e) {
      log.error(e.getMessage());
      throw new InfraException(ErrorCode.SMS_SEND_FAILED);
    }
  }
}
