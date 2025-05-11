package com.halo.eventer.infra.sms.naver;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.missing_person.MissingPersonFixture;
import com.halo.eventer.domain.missing_person.dto.MissingPersonReqDto;
import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.BaseException;
import com.halo.eventer.infra.sms.common.SmsSendRequest;
import com.halo.eventer.infra.sms.naver.dto.NaverSmsResDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class NaverSmsClientTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private NaverSmsClient smsClient;

    private static final String ACCESS_KEY = "test-access";
    private static final String SECRET_KEY = "test-secret";
    private static final String SENDER = "01012345678";
    private static final String SERVICE_ID = "svc-001";
    private static final String API_BASE = "https://sens.example.com";

    private MissingPersonReqDto missingPersonReqDto;

    @BeforeEach
    void init() {
        setField(smsClient, "accessKey", ACCESS_KEY);
        setField(smsClient, "secretKey", SECRET_KEY);
        setField(smsClient, "senderPhone", SENDER);
        setField(smsClient, "apiBaseUrl", API_BASE);
        setField(smsClient, "serviceId", SERVICE_ID);
        missingPersonReqDto = MissingPersonFixture.실종자_생성_DTO();
    }

    @Test
    void sendToMany_메서드_테스트() {
        // given
        SmsSendRequest smsSendRequest = SmsSendRequest.of("01099998888", missingPersonReqDto);
        List<SmsSendRequest> requests = List.of(smsSendRequest);
        String endpoint = "/sms/v2/services/" + SERVICE_ID + "/messages";
        String expectedUrl = API_BASE + endpoint;
        NaverSmsResDto fakeResponse = new NaverSmsResDto();
        setField(fakeResponse, "requestId", "requestId");
        setField(fakeResponse, "requestTime", LocalDateTime.now());
        setField(fakeResponse, "statusCode", "code");
        setField(fakeResponse, "statusName", "success");

        given(restTemplate.postForObject(eq(expectedUrl), any(HttpEntity.class), eq(NaverSmsResDto.class)))
                .willReturn(fakeResponse);

        NaverSmsResDto result = smsClient.sendToMany(requests);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getRequestId()).isEqualTo(fakeResponse.getRequestId());
        assertThat(result.getRequestTime()).isEqualTo(fakeResponse.getRequestTime());
        assertThat(result.getStatusCode()).isEqualTo(fakeResponse.getStatusCode());
        assertThat(result.getStatusName()).isEqualTo(fakeResponse.getStatusName());
        verify(restTemplate, times(1)).postForObject(eq(expectedUrl), any(HttpEntity.class), eq(NaverSmsResDto.class));
    }

    @Test
    void sendToMany_BaseException_예외() {
        // given
        List<SmsSendRequest> requests = List.of(mock(SmsSendRequest.class));
        String endpoint = "/sms/v2/services/" + SERVICE_ID + "/messages";
        String expectedUrl = API_BASE + endpoint;
        given(restTemplate.postForObject(eq(expectedUrl), any(HttpEntity.class), eq(NaverSmsResDto.class)))
                .willThrow(new RuntimeException("downstream failure"));

        assertThatThrownBy(() -> smsClient.sendToMany(requests))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.SMS_SEND_FAILED);
        verify(restTemplate).postForObject(eq(expectedUrl), any(HttpEntity.class), eq(NaverSmsResDto.class));
    }
}
