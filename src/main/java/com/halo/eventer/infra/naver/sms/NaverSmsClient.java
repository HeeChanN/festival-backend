package com.halo.eventer.infra.naver.sms;

import com.halo.eventer.infra.common.sms.SmsClient;
import com.halo.eventer.infra.naver.sms.dto.MessageDto;
import com.halo.eventer.infra.naver.sms.dto.SmsReqDto;
import com.halo.eventer.infra.naver.sms.dto.SmsResDto;
import com.halo.eventer.infra.naver.sms.util.SmsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NaverSmsClient implements SmsClient {

    private final SmsRequest smsRequest;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    @Override
    public void send(String message, List<String> phoneNumbers) {
        String endpoint = "/sms/v2/services/" + serviceId + "/messages";

        List<MessageDto> messages = new ArrayList<>();
        for (String phone : phoneNumbers) {
            MessageDto messageDto = MessageDto.builder()
                    .to(phone)
                    .content(message)
                    .build();
            messages.add(messageDto);
        }

        SmsReqDto smsReqDto = smsRequest.getSmsBody(messages, message);
        SmsResDto smsResDto = smsRequest.sendSmsReq(smsReqDto,endpoint);
        log.info("SMS 요청 결과: {}", smsResDto);
    }
}