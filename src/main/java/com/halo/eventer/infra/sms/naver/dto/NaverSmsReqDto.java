package com.halo.eventer.infra.sms.naver.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NaverSmsReqDto {
    private String type;
    private String contentType;
    private String countryCode;
    private String from;
    private String subject;
    private String content;
    private List<NaverMessageReqDto> messages;
}
