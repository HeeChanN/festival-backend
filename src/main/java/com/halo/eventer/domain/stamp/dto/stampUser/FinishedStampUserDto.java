package com.halo.eventer.domain.stamp.dto.stampUser;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FinishedStampUserDto {
    private String schoolNo;
    private String name;
    private String phone;

    public FinishedStampUserDto(String schoolNo, String name, String phone) {
        this.schoolNo = schoolNo;
        this.name = name;
        this.phone = phone;
    }
}
