package com.halo.eventer.domain.stamp.dto.stamp.request;

import jakarta.validation.constraints.NotNull;

import com.halo.eventer.domain.stamp.dto.stamp.enums.GuideDesignTemplate;
import com.halo.eventer.domain.stamp.dto.stamp.enums.GuideSlideMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampTourParticipateGuideReqDto {
    @NotNull
    private GuideDesignTemplate guideDesignTemplate;

    @NotNull
    private GuideSlideMethod guideSlideMethod;
}
