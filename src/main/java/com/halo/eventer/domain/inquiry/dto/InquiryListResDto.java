package com.halo.eventer.domain.inquiry.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquiryListResDto {
    List<InquiryItemDto> inquiryList;

    public InquiryListResDto(List<InquiryItemDto> inquiryList) {
        this.inquiryList = inquiryList;
    }
}
