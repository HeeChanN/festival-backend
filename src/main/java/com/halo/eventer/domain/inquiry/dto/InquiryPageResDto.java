package com.halo.eventer.domain.inquiry.dto;


import com.halo.eventer.global.common.page.PageInfo;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquiryPageResDto {
    private List<InquiryItemDto> inquiryList;
    private PageInfo pageInfo;

    public InquiryPageResDto(List<InquiryItemDto> inquiryList, PageInfo pageInfo) {
        this.inquiryList = inquiryList;
        this.pageInfo = pageInfo;
    }
}
