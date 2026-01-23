package com.halo.eventer.domain.stamp.dto.stamp.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.halo.eventer.domain.stamp.dto.stamp.enums.MediaSpec;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampTourParticipateGuidePageReqDto {
    @NotBlank
    private String title;

    @NotNull
    private MediaSpec mediaSpec;

    private String mediaUrl;

    private String summary;

    private String details;

    private String additional;
}
