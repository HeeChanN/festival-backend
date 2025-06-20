package com.halo.eventer.domain.stamp.dto.stampUser;

import lombok.Getter;

@Getter
// TODO : 하나의 dto로 통일한 후 schoolNo 필드의 null 여부를 확인하는 방향도 고려할 수 있음
public class SignupDto {
    private String name;
    private String phone;
    private int participantCount;
    private String schoolNo;
}
