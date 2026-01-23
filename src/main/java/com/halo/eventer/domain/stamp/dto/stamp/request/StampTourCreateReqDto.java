package com.halo.eventer.domain.stamp.dto.stamp.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampTourCreateReqDto {
    @NotBlank
    private String title;

    @NotNull
    private Boolean showStamp;
}
