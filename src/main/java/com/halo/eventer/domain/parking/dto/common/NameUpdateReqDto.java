package com.halo.eventer.domain.parking.dto.common;

import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NameUpdateReqDto {

    @NotEmpty
    private String name;
}
