package com.halo.eventer.domain.stamp.dto.stampUser;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;

@Getter
// TODO : 하나의 dto로 통일한 후 schoolNo 필드의 null 여부를 확인하는 방향도 고려할 수 있음
public class SignupDto {
    @NotEmpty
    private String name;

    @NotEmpty
    private String phone;

    @Min(1)
    private int participantCount;

    private String schoolNo;
}
