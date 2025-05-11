package com.halo.eventer.domain.stamp.dto.stampUser;

import lombok.Getter;

@Getter
public abstract class SignupDto {
    private String name;
    private String phone;
    private int participantCount;
}
