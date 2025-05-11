package com.halo.eventer.domain.inquiry.dto;

import java.time.LocalDateTime;

import com.halo.eventer.domain.inquiry.Inquiry;
import com.halo.eventer.domain.inquiry.InquiryConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class InquiryItemDto {
    private Long id;
    private String title;
    private Boolean isAnswered;
    private Boolean isSecret;
    private String userId;
    private LocalDateTime createdAt;

    public InquiryItemDto(Inquiry inquiry, String title, String userId) {
        this.id = inquiry.getId();
        this.isAnswered = inquiry.isAnswered();
        this.createdAt = inquiry.getCreatedAt();
        this.isSecret = inquiry.isSecret();
        this.title = title;
        this.userId = userId;
    }

    public InquiryItemDto(Inquiry inquiry) {
        this.id = inquiry.getId();
        this.isAnswered = inquiry.isAnswered();
        this.createdAt = inquiry.getCreatedAt();
        this.isSecret = inquiry.isSecret();
        this.title = inquiry.getTitle();
        this.userId = inquiry.getUserId();
    }

    public void changeTitleToSecretValue() {
        if (this.isSecret) {
            title = InquiryConstants.PRIVATE_INQUIRY_TITLE;
        }
    }
}
