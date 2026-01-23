package com.halo.eventer.domain.stamp.dto.stampUser.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PrizeClaimQrResDto {
    private String userName;
    private String phone;
    private int participateCount;
    private String extraText;

    public static PrizeClaimQrResDto from(String name, String phone, int participateCount, String extraText) {
        return new PrizeClaimQrResDto(name, phone, participateCount, extraText);
    }
}
