package com.halo.eventer.domain.stamp.dto.stamp.response;

import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.dto.stamp.enums.PrizeExchangeImgType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PrizeExchangeImgResDto {
    private PrizeExchangeImgType prizeExchangeImgType;
    private String prizeExchangeCustomImgUrl;

    public static PrizeExchangeImgResDto from(Stamp stamp) {
        if (stamp.getPrizeExchangeImgType() == PrizeExchangeImgType.DEFAULT) {
            return new PrizeExchangeImgResDto(PrizeExchangeImgType.DEFAULT, null);
        }
        return new PrizeExchangeImgResDto(PrizeExchangeImgType.CUSTOM, stamp.getPrizeExchangeCustomImgUrl());
    }
}
