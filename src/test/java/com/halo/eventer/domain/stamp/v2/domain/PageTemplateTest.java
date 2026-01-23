package com.halo.eventer.domain.stamp.v2.domain;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.stamp.PageTemplate;
import com.halo.eventer.domain.stamp.Stamp;

import static com.halo.eventer.domain.festival.FestivalFixture.축제_엔티티;
import static com.halo.eventer.domain.stamp.dto.stamp.enums.ButtonLayout.*;
import static com.halo.eventer.domain.stamp.v2.fixture.ButtonFixture.*;
import static com.halo.eventer.domain.stamp.v2.fixture.PageTemplateFixture.*;
import static com.halo.eventer.domain.stamp.v2.fixture.StampTourFixture.스탬프투어1_생성;
import static org.assertj.core.api.AssertionsForClassTypes.*;

@SuppressWarnings("NonAsciiCharacters")
public class PageTemplateTest {

    private Festival 제주도_축제;
    private Stamp 스탬프투어1;
    private PageTemplate 메인페이지;
    private PageTemplate 랜딩페이지;

    @BeforeEach
    void setUp() {
        제주도_축제 = 축제_엔티티();
        스탬프투어1 = 스탬프투어1_생성(제주도_축제);
        랜딩페이지 = 랜딩페이지_생성(스탬프투어1);
        메인페이지 = 메인페이지_생성(스탬프투어1);
    }

    @Test
    void 랜딩페이지_정보_수정시_버튼과_디자인이_적용된다() {
        var 랜딩페이지_업데이트 = 랜딩페이지_업데이트_요청();

        랜딩페이지.updateLandingPageTemplate(랜딩페이지_업데이트);

        assertThat(랜딩페이지.getBackgroundImg()).isEqualTo(랜딩페이지_업데이트.getBackgroundImgUrl());
        assertThat(랜딩페이지.getIconImg()).isEqualTo(랜딩페이지_업데이트.getIconImgUrl());
        assertThat(랜딩페이지.getDescription()).isEqualTo(랜딩페이지_업데이트.getDescription());
        assertThat(랜딩페이지.getButtonLayout()).isEqualTo(랜딩페이지_업데이트.getButtonLayout());
        assertThat(랜딩페이지.getButtons().size()).isEqualTo(2);
    }

    @Test
    void 메인페이지_정보_수정시_버튼과_디자인이_적용된다() {
        var 메인페이지_업데이트 = 메인페이지_업데이트_요청();

        메인페이지.updateMainPageTemplate(메인페이지_업데이트);

        assertThat(메인페이지.getBackgroundImg()).isEqualTo(메인페이지_업데이트.getBackgroundImgUrl());
        assertThat(메인페이지.getMainPageDesignTemplate()).isEqualTo(메인페이지_업데이트.getMainPageDesignTemplate());
        assertThat(메인페이지.getButtons().size()).isEqualTo(2);
    }

    @Test
    void 버튼_시퀀스가_중복되면_예외가_발생한다() {
        var 중복된버튼리스트 = 중복된_시퀀스_버튼리스트();

        assertThatThrownBy(() -> 메인페이지.updateButtons(중복된버튼리스트))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("중복된 sequenceIndex");
    }

    @Nested
    class 랜딩페이지_버튼_레이아웃별_테스트 {

        @Test
        void 랜딩페이지_버튼레이아웃_ONE_버튼_2개_예외() {
            var 요청 = 랜딩페이지_요청_생성(ONE, 버튼2개());
            assertThatThrownBy(() -> 랜딩페이지.updateLandingPageTemplate(요청))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("버튼 1개만 허용");
        }

        @Test
        void 랜딩페이지_버튼레이아웃_ONE_버튼_1개_성공() {
            var 요청 = 랜딩페이지_요청_생성(ONE, 버튼1개());

            랜딩페이지.updateLandingPageTemplate(요청);

            assertThat(랜딩페이지.getButtonLayout()).isEqualTo(ONE);
            assertThat(랜딩페이지.getButtons().size()).isEqualTo(1);
            assertThat(랜딩페이지.getButtons().get(0).getContent()).isEqualTo("버튼1");
        }

        @Test
        void 랜딩페이지_버튼레이아웃_TWO_SYM_버튼_2개_성공() {
            var 요청 = 랜딩페이지_요청_생성(TWO_SYM, 버튼2개());

            랜딩페이지.updateLandingPageTemplate(요청);

            assertThat(랜딩페이지.getButtonLayout()).isEqualTo(TWO_SYM);
            assertThat(랜딩페이지.getButtons().size()).isEqualTo(2);
        }

        @Test
        void 랜딩페이지_버튼레이아웃_TWO_SYM_버튼_1개_예외() {
            var 요청 = 랜딩페이지_요청_생성(TWO_SYM, 버튼1개());

            assertThatThrownBy(() -> 랜딩페이지.updateLandingPageTemplate(요청))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("버튼 2개만 허용");
        }

        @Test
        void 랜딩페이지_버튼레이아웃_TWO_ASYM_버튼_2개_성공() {
            var 요청 = 랜딩페이지_요청_생성(TWO_ASYM, 버튼2개());

            랜딩페이지.updateLandingPageTemplate(요청);

            assertThat(랜딩페이지.getButtonLayout()).isEqualTo(TWO_ASYM);
            assertThat(랜딩페이지.getButtons().size()).isEqualTo(2);
        }

        @Test
        void 랜딩페이지_버튼레이아웃_NONE_버튼_없음_성공() {
            var 요청 = 랜딩페이지_요청_생성(NONE, List.of());

            랜딩페이지.updateLandingPageTemplate(요청);

            assertThat(랜딩페이지.getButtonLayout()).isEqualTo(NONE);
            assertThat(랜딩페이지.getButtons().size()).isEqualTo(0);
        }
    }

    @Nested
    class 메인페이지_버튼_레이아웃별_테스트 {

        @Test
        void 메인페이지_버튼레이아웃_ONE_버튼_2개_예외() {
            var 요청 = 메인페이지_요청_생성(ONE, 버튼2개());

            assertThatThrownBy(() -> 메인페이지.updateMainPageTemplate(요청))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("버튼 1개만 허용");
        }

        @Test
        void 메인페이지_버튼레이아웃_ONE_버튼_1개_성공() {
            var 요청 = 메인페이지_요청_생성(ONE, 버튼1개());

            메인페이지.updateMainPageTemplate(요청);

            assertThat(메인페이지.getButtonLayout()).isEqualTo(ONE);
            assertThat(메인페이지.getButtons().size()).isEqualTo(1);
            assertThat(메인페이지.getButtons().get(0).getContent()).isEqualTo("버튼1");
        }

        @Test
        void 메인페이지_버튼레이아웃_TWO_SYM_버튼_2개_성공() {
            var 요청 = 메인페이지_요청_생성(TWO_SYM, 버튼2개());

            메인페이지.updateMainPageTemplate(요청);

            assertThat(메인페이지.getButtonLayout()).isEqualTo(TWO_SYM);
            assertThat(메인페이지.getButtons().size()).isEqualTo(2);
        }

        @Test
        void 메인페이지_버튼레이아웃_TWO_SYM_버튼_1개_예외() {
            var 요청 = 메인페이지_요청_생성(TWO_SYM, 버튼1개());

            assertThatThrownBy(() -> 메인페이지.updateMainPageTemplate(요청))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("버튼 2개만 허용");
        }

        @Test
        void 메인페이지_버튼레이아웃_TWO_ASYM_버튼_2개_성공() {
            var 요청 = 메인페이지_요청_생성(TWO_ASYM, 버튼2개());

            메인페이지.updateMainPageTemplate(요청);

            assertThat(메인페이지.getButtonLayout()).isEqualTo(TWO_ASYM);
            assertThat(메인페이지.getButtons().size()).isEqualTo(2);
        }

        @Test
        void 메인페이지_버튼레이아웃_NONE_버튼_없음_성공() {
            var 요청 = 메인페이지_요청_생성(NONE, List.of());

            메인페이지.updateMainPageTemplate(요청);

            assertThat(메인페이지.getButtonLayout()).isEqualTo(NONE);
            assertThat(메인페이지.getButtons().size()).isEqualTo(0);
        }
    }
}
