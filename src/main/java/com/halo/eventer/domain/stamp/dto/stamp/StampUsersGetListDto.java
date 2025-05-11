package com.halo.eventer.domain.stamp.dto.stamp;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StampUsersGetListDto {
    private List<StampUsersGetDto> stampUsers;

    public StampUsersGetListDto(List<StampUsersGetDto> stampUsers) {
        this.stampUsers = stampUsers;
    }
}
