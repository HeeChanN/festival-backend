package com.halo.eventer.domain.inquiry.dto;

import java.time.LocalDateTime;

import com.halo.eventer.domain.inquiry.Inquiry;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquiryResDto {
    private String title;
    private String userId;
    private LocalDateTime createdDate;
    private String content;
    private String answer;
    private Boolean isAnswered;

    public InquiryResDto(Inquiry inquiry) {
        this.title = inquiry.getTitle();
        this.userId = inquiry.getUserId();
        this.createdDate = inquiry.getCreatedAt();
        this.content = inquiry.getContent();
        this.answer = inquiry.getAnswer();
        this.isAnswered = inquiry.isAnswered();
    }
}
