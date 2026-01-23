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
public class StampTourParticipateGuideResDto {
    private GuideDesignTemplate guideDesignTemplate;
    private GuideSlideMethod guideSlideMethod;
    private List<ParticipateGuidePageSummaryResDto> participateGuidePages;

    public static StampTourParticipateGuideResDto from(ParticipateGuide guide) {
        return new StampTourParticipateGuideResDto(
                guide.getGuideDesignTemplate(),
                guide.getGuideSlideMethod(),
                ParticipateGuidePageSummaryResDto.fromEntities(guide.getParticipateGuidePages()));
    }
}
