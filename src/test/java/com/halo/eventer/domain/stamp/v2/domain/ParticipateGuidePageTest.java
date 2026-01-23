package com.halo.eventer.domain.stamp.v2.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.stamp.ParticipateGuide;
import com.halo.eventer.domain.stamp.ParticipateGuidePage;
import com.halo.eventer.domain.stamp.Stamp;

import static com.halo.eventer.domain.festival.FestivalFixture.축제_엔티티;
import static com.halo.eventer.domain.stamp.v2.fixture.ParticipateGuideFixture.참여가이드_기본값;
import static com.halo.eventer.domain.stamp.v2.fixture.ParticipateGuidePageFixture.*;
import static com.halo.eventer.domain.stamp.v2.fixture.StampTourFixture.스탬프투어1_생성;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class ParticipateGuidePageTest {

    private Festival 축제;
    private Stamp 스탬프;
    private ParticipateGuide 참여가이드;
    private ParticipateGuidePage 참여방법_페이지;

    @BeforeEach
    void setUp() {
        축제 = 축제_엔티티();
        스탬프 = 스탬프투어1_생성(축제);
        참여가이드 = 참여가이드_기본값(스탬프);
        참여방법_페이지 = 참여방법_페이지1(참여가이드);
    }

    @Test
    void 참여가이드_페이지_1_생성_검증() {
        assertThat(참여방법_페이지.getTitle()).isEqualTo(참여방법_페이지1_제목);
        assertThat(참여방법_페이지.getMediaSpec()).isEqualTo(참여방법_페이지1_미디어_제공_형식);
        assertThat(참여방법_페이지.getMediaUrl()).isEqualTo(참여방법_페이지1_미디어_url);
        assertThat(참여방법_페이지.getSummary()).isEqualTo(참여방법_페이지1_요약);
        assertThat(참여방법_페이지.getDetails()).isEqualTo(참여방법_페이지1_상세);
        assertThat(참여방법_페이지.getAdditional()).isEqualTo(참여방법_페이지1_추가);
        assertThat(참여방법_페이지.getParticipateGuide()).isEqualTo(참여가이드);
        assertThat(참여가이드.getParticipateGuidePages()).contains(참여방법_페이지);
    }

    @Test
    void 참여가이드_페이지_리스트_5개_생성() {
        var 페이지들 = 페이지_리스트_생성(참여가이드);
        assertThat(페이지들).hasSize(5);
        assertThat(참여가이드.getParticipateGuidePages()).containsAll(페이지들);
    }

    @Test
    void 참여가이드_페이지_수정_정상작동() {
        var 참여가이드_페이지_수정 = 페이지_수정();
        참여방법_페이지.update(참여가이드_페이지_수정);
        assertThat(참여방법_페이지.getTitle()).isEqualTo(참여방법_수정_제목);
        assertThat(참여방법_페이지.getMediaSpec()).isEqualTo(참여방법_수정_미디어_제공_형식);
        assertThat(참여방법_페이지.getMediaUrl()).isEqualTo(참여방법_수정_미디어_url);
        assertThat(참여방법_페이지.getSummary()).isEqualTo(참여방법_수정_요약);
        assertThat(참여방법_페이지.getDetails()).isEqualTo(참여방법_수정_상세);
        assertThat(참여방법_페이지.getAdditional()).isEqualTo(참여방법_수정_추가);
    }

    @Test
    void 참여가이드_페이지_디스플레이_순서_변경() {
        참여방법_페이지.updateDisplayOrder(3);
        assertThat(참여방법_페이지.getDisplayOrderFeature().getDisplayOrder()).isEqualTo(3);
    }
}
