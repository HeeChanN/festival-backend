package com.halo.eventer.domain.inquiry.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class InquiryAnswerReqDto {
    private String answer;

    public InquiryAnswerReqDto(String answer) {
        this.answer = answer;
    }
}
