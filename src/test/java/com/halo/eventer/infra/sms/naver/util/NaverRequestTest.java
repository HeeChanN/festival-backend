package com.halo.eventer.infra.sms.naver.util;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.infra.sms.common.SmsSendRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class NaverRequestTest {
    private static final String ACCESS_KEY = "test-access";
    private static final String SECRET_KEY = "test-secret";
    private static final String SENDER = "01012345678";
    private static final String ENDPOINT = "/sms/v2/services/svc-001/messages";

    @Test
    void 빌드_성공하면_유효한_HttpEntity_반환() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SmsSendRequest smsRequest1 = SmsSendRequest.of("01011111111", "message");
        SmsSendRequest smsRequest2 = SmsSendRequest.of("01022222222", "message");
        List<SmsSendRequest> requests = List.of(smsRequest1, smsRequest2);
        HttpEntity<String> entity = NaverRequest.builder(mapper, ACCESS_KEY, SECRET_KEY, SENDER)
                .header(ENDPOINT)
                .body(requests)
                .build();

        HttpHeaders headers = entity.getHeaders();
        assertThat(headers.getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(headers).containsKeys("x-ncp-iam-access-key", "x-ncp-apigw-timestamp", "x-ncp-apigw-signature-v2");

        Map<String, Object> payload = mapper.readValue(entity.getBody(), new TypeReference<Map<String, Object>>() {});
        assertThat(payload)
                .containsEntry("type", "LMS")
                .containsEntry("contentType", "COMM")
                .containsEntry("countryCode", "82")
                .containsEntry("from", SENDER)
                .containsEntry("content", "Default message");

        @SuppressWarnings("unchecked")
        List<Map<String, String>> msgList = (List<Map<String, String>>) payload.get("messages");
        assertThat(msgList).hasSize(2);
        assertThat(msgList.get(0)).containsEntry("to", "01011111111").containsEntry("content", "message");
        assertThat(msgList.get(1)).containsEntry("to", "01022222222").containsEntry("content", "message");
    }
}
