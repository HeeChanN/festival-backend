package com.halo.eventer.domain.inquiry.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class InquiryCreateReqDto {

    private String title;
    private Boolean isSecret;
    private String userId;
    private String password;
    private String content;

    public InquiryCreateReqDto(String title, Boolean isSecret, String userId, String password, String content) {
        this.title = title;
        this.isSecret = isSecret;
        this.userId = userId;
        this.password = password;
        this.content = content;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
