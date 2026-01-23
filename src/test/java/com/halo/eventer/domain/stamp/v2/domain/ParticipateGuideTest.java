package com.halo.eventer.domain.stamp.v2.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.stamp.ParticipateGuide;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.v2.fixture.ParticipateGuideFixture;
import com.halo.eventer.domain.stamp.v2.fixture.StampTourFixture;

import static com.halo.eventer.domain.stamp.v2.fixture.ParticipateGuideFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class ParticipateGuideTest {

    private Festival 축제;
    private Stamp 축제_스탬프투어;
    private ParticipateGuide 축제_스탬프투어_참여가이드;

    @BeforeEach
    public void setUp() {
        축제 = FestivalFixture.축제_엔티티();
        축제_스탬프투어 = StampTourFixture.스탬프투어1_생성(축제);
        축제_스탬프투어_참여가이드 = ParticipateGuideFixture.참여가이드_기본값(축제_스탬프투어);
    }

    @Test
    void 기본값으로_참여가이드를_생성한다() {
        assertThat(축제_스탬프투어_참여가이드.getGuideDesignTemplate()).isEqualTo(참여가이드_디자인_템플릿);
        assertThat(축제_스탬프투어_참여가이드.getGuideSlideMethod()).isEqualTo(참여가이드_슬라이드_방식);
        assertThat(축제_스탬프투어_참여가이드.getStamp()).isEqualTo(축제_스탬프투어);
        assertThat(축제_스탬프투어.getParticipationGuides()).contains(축제_스탬프투어_참여가이드);
    }

    @Test
    void 참여가이드_정보를_업데이트할_수_있다() {
        축제_스탬프투어_참여가이드.update(바뀐_참여가이드_디자인_템플릿, 바뀐_참여가이드_슬라이드_방식);

        assertThat(축제_스탬프투어_참여가이드.getGuideDesignTemplate()).isEqualTo(바뀐_참여가이드_디자인_템플릿);
        assertThat(축제_스탬프투어_참여가이드.getGuideSlideMethod()).isEqualTo(바뀐_참여가이드_슬라이드_방식);
    }
}
