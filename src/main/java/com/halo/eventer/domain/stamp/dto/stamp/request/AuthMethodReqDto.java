package com.halo.eventer.domain.stamp.dto.stamp.request;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthMethodReqDto {
    @NotNull
    private long code;
}
