package com.halo.eventer.domain.manager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ManagerResDto {
    private Long id;
    private String phoneNo;

    public ManagerResDto(Long id, String phoneNo) {
        this.id = id;
        this.phoneNo = phoneNo;
    }
}
