package com.halo.eventer.domain.festival.dto;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FestivalCreateDto {

    @NotNull
    private String name;

    @NotNull
    private String subDomain;
}
