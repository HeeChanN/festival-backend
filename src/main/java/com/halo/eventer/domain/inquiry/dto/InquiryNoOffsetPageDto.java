package com.halo.eventer.domain.inquiry.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.domain.inquiry.Inquiry;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquiryNoOffsetPageDto {
    private List<InquiryItemDto> inquiries;
    private Boolean isLast;

    public InquiryNoOffsetPageDto(List<Inquiry> inquiryList, Boolean isLast) {
        this.inquiries = inquiryList.stream().map(InquiryItemDto::new).collect(Collectors.toList());
        this.isLast = isLast;
    }

    public void updateInquiryItemDtoWithTitleVisibility() {
        this.inquiries.forEach(InquiryItemDto::changeTitleToSecretValue);
    }
}
