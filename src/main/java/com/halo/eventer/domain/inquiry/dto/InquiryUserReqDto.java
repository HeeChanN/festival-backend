package com.halo.eventer.domain.inquiry.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class InquiryUserReqDto {
    private String userId;
    private String password;

    public InquiryUserReqDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
