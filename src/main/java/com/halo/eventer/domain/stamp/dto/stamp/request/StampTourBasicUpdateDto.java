package com.halo.eventer.domain.stamp.dto.stamp.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampTourBasicUpdateDto {
    @NotBlank
    private String newTitle;

    @NotNull
    private boolean activation;

    @NotBlank
    private String authMethod;

    @NotBlank
    private String boothPassword;
}
