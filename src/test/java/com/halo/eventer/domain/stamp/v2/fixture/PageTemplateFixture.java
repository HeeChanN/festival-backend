package com.halo.eventer.domain.stamp.v2.fixture;

import java.util.List;

import com.halo.eventer.domain.stamp.PageTemplate;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.dto.stamp.enums.*;
import com.halo.eventer.domain.stamp.dto.stamp.request.ButtonReqDto;
import com.halo.eventer.domain.stamp.dto.stamp.request.StampTourLandingPageReqDto;
import com.halo.eventer.domain.stamp.dto.stamp.request.StampTourMainPageReqDto;

import static com.halo.eventer.domain.stamp.v2.fixture.ButtonFixture.버튼2개;

@SuppressWarnings("NonAsciiCharacters")
public class PageTemplateFixture {
    public static long 랜딩페이지_ID = 1L;
    public static PageType 랜딩페이지_타입 = PageType.LANDING;
    public static LandingPageDesignTemplate 랜딩페이지_디자인_템플릿 = LandingPageDesignTemplate.NONE;
    public static String 랜딩페이지_배경_이미지 = "배경.jpg";
    public static String 랜딩페이지_로고_이미지 = "로고.jpg";
    public static String 랜딩페이지_상세_설명 = "상 세 설 명";
    public static ButtonLayout 랜딩페이지_버튼_컴포넌트 = ButtonLayout.TWO_ASYM;

    public static LandingPageDesignTemplate 바뀐_랜딩페이지_디자인_템플릿 = LandingPageDesignTemplate.NONE;
    public static String 바뀐_랜딩페이지_배경_이미지 = "background.jpg";
    public static String 바뀐_랜딩페이지_아이콘_이미지 = "icon.jpg";
    public static String 바뀐_랜딩페이지_설명 = "설명 텍스트입니다.";
    public static ButtonLayout 바뀐_랜딩페이지_버튼_배치_형식 = ButtonLayout.TWO_SYM;

    public static long 메인페이지_ID = 2L;
    public static PageType 메인페이지_타입 = PageType.MAIN;
    public static MainPageDesignTemplate 메인페이지_디자인_템플릿 = MainPageDesignTemplate.GRID_Nx2;
    public static String 메인페이지_배경_이미지 = "배경.jpg";
    public static ButtonLayout 메인페이지_버튼_컴포넌트 = ButtonLayout.TWO_ASYM;

    public static MainPageDesignTemplate 바뀐_메인페이지_디자인_템플릿 = MainPageDesignTemplate.GRID_Nx3;
    public static String 바뀐_메인페이지_배경_이미지 = "main_background.jpg";
    public static ButtonLayout 바뀐_바뀐_메인페이지_버튼_배치_형식 = ButtonLayout.TWO_SYM;

    public static PageTemplate 랜딩페이지_생성(Stamp stamp) {
        return PageTemplate.defaultLandingPage(stamp);
    }

    public static PageTemplate 메인페이지_생성(Stamp stamp) {
        return PageTemplate.defaultMainPage(stamp);
    }

    public static StampTourLandingPageReqDto 랜딩페이지_업데이트_요청() {
        return new StampTourLandingPageReqDto(
                바뀐_랜딩페이지_디자인_템플릿, 바뀐_랜딩페이지_배경_이미지, 바뀐_랜딩페이지_아이콘_이미지, 바뀐_랜딩페이지_설명, 바뀐_랜딩페이지_버튼_배치_형식, 버튼2개());
    }

    public static StampTourMainPageReqDto 메인페이지_업데이트_요청() {
        return new StampTourMainPageReqDto(바뀐_메인페이지_디자인_템플릿, 바뀐_메인페이지_배경_이미지, 바뀐_바뀐_메인페이지_버튼_배치_형식, 버튼2개());
    }

    public static StampTourLandingPageReqDto 랜딩페이지_요청_생성(ButtonLayout layout, List<ButtonReqDto> 버튼들) {
        return new StampTourLandingPageReqDto(
                바뀐_랜딩페이지_디자인_템플릿, 바뀐_랜딩페이지_배경_이미지, 바뀐_랜딩페이지_아이콘_이미지, 바뀐_랜딩페이지_설명, layout, 버튼들);
    }

    public static StampTourMainPageReqDto 메인페이지_요청_생성(ButtonLayout layout, List<ButtonReqDto> 버튼들) {
        return new StampTourMainPageReqDto(바뀐_메인페이지_디자인_템플릿, 바뀐_메인페이지_배경_이미지, layout, 버튼들);
    }
}
