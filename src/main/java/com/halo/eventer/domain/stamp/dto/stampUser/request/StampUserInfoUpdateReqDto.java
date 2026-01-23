package com.halo.eventer.domain.stamp.dto.stampUser.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampUserInfoUpdateReqDto {
    private String name;
    private String phone;
    private int participantCount;
    private String extraText;
}
