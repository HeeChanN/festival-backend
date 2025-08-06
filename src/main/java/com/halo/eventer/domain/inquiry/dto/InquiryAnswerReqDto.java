package com.halo.eventer.domain.inquiry.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class InquiryAnswerReqDto {

    @NotNull
    @Size(max = 500)
    private String answer;

    public InquiryAnswerReqDto(String answer) {
        this.answer = answer;
    }
}
