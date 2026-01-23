package com.halo.eventer.domain.stamp.v2.fixture;

import com.halo.eventer.domain.stamp.ParticipateGuide;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.dto.stamp.enums.GuideDesignTemplate;
import com.halo.eventer.domain.stamp.dto.stamp.enums.GuideSlideMethod;

@SuppressWarnings("NonAsciiCharacters")
public class ParticipateGuideFixture {

    public static long 참여가이드_ID = 1L;
    public static GuideDesignTemplate 참여가이드_디자인_템플릿 = GuideDesignTemplate.FULL;
    public static GuideSlideMethod 참여가이드_슬라이드_방식 = GuideSlideMethod.SLIDE;

    public static GuideDesignTemplate 바뀐_참여가이드_디자인_템플릿 = GuideDesignTemplate.NON_FULL;
    public static GuideSlideMethod 바뀐_참여가이드_슬라이드_방식 = GuideSlideMethod.SLIDE;

    public static ParticipateGuide 참여가이드_기본값(Stamp stamp) {
        return ParticipateGuide.defaultParticipateGuide(stamp);
    }
}
