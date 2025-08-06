package com.halo.eventer.domain.inquiry.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.halo.eventer.domain.inquiry.Inquiry;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquiryResDto {
    private String title;
    private String userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    private LocalDateTime createdAt;

    private String content;
    private String answer;
    private Boolean isAnswered;

    public InquiryResDto(Inquiry inquiry) {
        this.title = inquiry.getTitle();
        this.userId = inquiry.getUserId();
        this.createdAt = inquiry.getCreatedAt();
        this.content = inquiry.getContent();
        this.answer = inquiry.getAnswer();
        this.isAnswered = inquiry.isAnswered();
    }
}
