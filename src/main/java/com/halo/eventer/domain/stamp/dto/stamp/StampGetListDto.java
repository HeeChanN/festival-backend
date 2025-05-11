package com.halo.eventer.domain.stamp.dto.stamp;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StampGetListDto {
    private List<StampGetDto> stampGetDtos;

    public StampGetListDto(List<StampGetDto> stampGetDtos) {
        this.stampGetDtos = stampGetDtos;
    }
}
