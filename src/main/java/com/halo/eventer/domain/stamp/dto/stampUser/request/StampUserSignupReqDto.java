package com.halo.eventer.domain.stamp.dto.stampUser.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampUserSignupReqDto {

    private String name;
    private String phone;
    private Integer participantCount;
    private String extraText;
}
