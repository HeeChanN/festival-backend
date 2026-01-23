package com.halo.eventer.domain.stamp.dto.stamp.response;

import java.util.List;

import com.halo.eventer.domain.stamp.ParticipateGuide;
import com.halo.eventer.domain.stamp.dto.stamp.enums.GuideDesignTemplate;
import com.halo.eventer.domain.stamp.dto.stamp.enums.GuideSlideMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampTourGuideResDto {
    private long participateGuideId;
    private GuideDesignTemplate guideDesignTemplate;
    private GuideSlideMethod guideSlideMethod;
    private List<StampTourGuidePageResDto> participateGuidePages;

    public static StampTourGuideResDto from(ParticipateGuide guide) {
        return new StampTourGuideResDto(
                guide.getId(),
                guide.getGuideDesignTemplate(),
                guide.getGuideSlideMethod(),
                StampTourGuidePageResDto.fromEntities(guide.getParticipateGuidePages()));
    }
}
