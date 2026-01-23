package com.halo.eventer.domain.stamp.dto.stamp.request;

import com.halo.eventer.domain.stamp.dto.stamp.enums.PrizeExchangeImgType;
import lombok.Getter;

@Getter
public class PrizeExchangeImgReqDto {
    private PrizeExchangeImgType prizeExchangeImgType;
    private String customExchangeImgUrl;
}
