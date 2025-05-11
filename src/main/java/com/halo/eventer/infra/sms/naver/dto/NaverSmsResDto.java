package com.halo.eventer.infra.sms.naver.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Builder
@ToString(of = {"requestId", "statusCode", "statusName"})
@AllArgsConstructor
@NoArgsConstructor
public class NaverSmsResDto {
    private String requestId;
    private LocalDateTime requestTime;
    private String statusCode;
    private String statusName;
}
