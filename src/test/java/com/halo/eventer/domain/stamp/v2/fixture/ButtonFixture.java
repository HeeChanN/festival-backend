package com.halo.eventer.domain.stamp.v2.fixture;

import java.util.List;

import com.halo.eventer.domain.stamp.dto.stamp.enums.ButtonAction;
import com.halo.eventer.domain.stamp.dto.stamp.request.ButtonReqDto;

@SuppressWarnings("NonAsciiCharacters")
public class ButtonFixture {
    public static final int 버튼1_순서 = 0;
    public static final String 버튼1_아이콘 = "icon1.jpg";
    public static final String 버튼1_텍스트 = "버튼1";
    public static final ButtonAction 버튼1_행동 = ButtonAction.OPEN_URL;
    public static final String 버튼1_링크 = "https://link1.com";

    public static final int 버튼2_순서 = 1;
    public static final String 버튼2_아이콘 = "icon2.jpg";
    public static final String 버튼2_텍스트 = "버튼2";
    public static final ButtonAction 버튼2_행동 = ButtonAction.OPEN_URL;
    public static final String 버튼2_링크 = "https://link2.com";

    public static ButtonReqDto 버튼1_요청() {
        return new ButtonReqDto(버튼1_순서, 버튼1_아이콘, 버튼1_텍스트, 버튼1_행동, 버튼1_링크);
    }

    public static ButtonReqDto 버튼2_요청() {
        return new ButtonReqDto(버튼2_순서, 버튼2_아이콘, 버튼2_텍스트, 버튼2_행동, 버튼2_링크);
    }

    public static List<ButtonReqDto> 버튼1개() {
        return List.of(버튼1_요청());
    }

    public static List<ButtonReqDto> 버튼2개() {
        return List.of(버튼1_요청(), 버튼2_요청());
    }

    public static List<ButtonReqDto> 빈버튼() {
        return List.of();
    }

    public static List<ButtonReqDto> 중복된_시퀀스_버튼리스트() {
        return List.of(
                new ButtonReqDto(0, "icon1.jpg", "메인버튼1", ButtonAction.OPEN_URL, "https://main1.com"),
                new ButtonReqDto(0, "icon2.jpg", "메인버튼2", ButtonAction.OPEN_URL, "https://main2.com"));
    }
}
