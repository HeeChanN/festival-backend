package com.halo.eventer.domain.stamp.v2.fixture;

import java.util.List;

import com.halo.eventer.domain.stamp.ParticipateGuide;
import com.halo.eventer.domain.stamp.ParticipateGuidePage;
import com.halo.eventer.domain.stamp.dto.stamp.enums.MediaSpec;
import com.halo.eventer.domain.stamp.dto.stamp.request.StampTourParticipateGuidePageReqDto;

@SuppressWarnings("NonAsciiCharacters")
public class ParticipateGuidePageFixture {

    public static long 참여방법_페이지1_ID = 1L;
    public static String 참여방법_페이지1_제목 = "방법 페이지 이름 1";
    public static MediaSpec 참여방법_페이지1_미디어_제공_형식 = MediaSpec.NONE;
    public static String 참여방법_페이지1_미디어_url = "img1 url";
    public static String 참여방법_페이지1_요약 = "요약 1";
    public static String 참여방법_페이지1_상세 = "상세 1";
    public static String 참여방법_페이지1_추가 = "추가 1";

    public static long 참여방법_페이지2_ID = 2L;
    public static String 참여방법_페이지2_제목 = "방법 페이지 이름 2";
    public static MediaSpec 참여방법_페이지2_미디어_제공_형식 = MediaSpec.NONE;

    public static String 참여방법_페이지2_미디어_url = "img2 url";
    public static String 참여방법_페이지2_요약 = "요약 2";
    public static String 참여방법_페이지2_상세 = "상세 2";
    public static String 참여방법_페이지2_추가 = "추가 2";

    public static long 참여방법_페이지3_ID = 3L;
    public static String 참여방법_페이지3_제목 = "방법 페이지 이름 3";
    public static MediaSpec 참여방법_페이지3_미디어_제공_형식 = MediaSpec.NONE;
    public static String 참여방법_페이지3_미디어_url = "img3 url";
    public static String 참여방법_페이지3_요약 = "요약 3";
    public static String 참여방법_페이지3_상세 = "상세 3";
    public static String 참여방법_페이지3_추가 = "추가 3";

    public static long 참여방법_페이지4_ID = 4L;
    public static String 참여방법_페이지4_제목 = "방법 페이지 이름 4";
    public static MediaSpec 참여방법_페이지4_미디어_제공_형식 = MediaSpec.NONE;
    public static String 참여방법_페이지4_미디어_url = "img4 url";
    public static String 참여방법_페이지4_요약 = "요약 4";
    public static String 참여방법_페이지4_상세 = "상세 4";
    public static String 참여방법_페이지4_추가 = "추가 4";

    public static long 참여방법_페이지5_ID = 5L;
    public static String 참여방법_페이지5_제목 = "방법 페이지 이름 5";
    public static MediaSpec 참여방법_페이지5_미디어_제공_형식 = MediaSpec.NONE;
    public static String 참여방법_페이지5_미디어_url = "img5 url";
    public static String 참여방법_페이지5_요약 = "요약 5";
    public static String 참여방법_페이지5_상세 = "상세 5";
    public static String 참여방법_페이지5_추가 = "추가 5";

    public static String 참여방법_수정_제목 = "수정된 제목";
    public static MediaSpec 참여방법_수정_미디어_제공_형식 = MediaSpec.SIXTEEN_TO_NINE;
    public static String 참여방법_수정_미디어_url = "수정된 이미지 url";
    public static String 참여방법_수정_요약 = "수정된 요약";
    public static String 참여방법_수정_상세 = "수정된 상세";
    public static String 참여방법_수정_추가 = "수정된 추가";

    public static ParticipateGuidePage 참여방법_페이지1(ParticipateGuide participateGuide) {
        return ParticipateGuidePage.from(
                참여방법_페이지1_제목,
                참여방법_페이지1_미디어_제공_형식,
                참여방법_페이지1_미디어_url,
                참여방법_페이지1_요약,
                참여방법_페이지1_상세,
                참여방법_페이지1_추가,
                participateGuide);
    }

    public static ParticipateGuidePage 참여방법_페이지2(ParticipateGuide participateGuide) {
        return ParticipateGuidePage.from(
                참여방법_페이지2_제목,
                참여방법_페이지2_미디어_제공_형식,
                참여방법_페이지2_미디어_url,
                참여방법_페이지2_요약,
                참여방법_페이지2_상세,
                참여방법_페이지2_추가,
                participateGuide);
    }

    public static ParticipateGuidePage 참여방법_페이지3(ParticipateGuide participateGuide) {
        return ParticipateGuidePage.from(
                참여방법_페이지3_제목,
                참여방법_페이지3_미디어_제공_형식,
                참여방법_페이지3_미디어_url,
                참여방법_페이지3_요약,
                참여방법_페이지3_상세,
                참여방법_페이지3_추가,
                participateGuide);
    }

    public static ParticipateGuidePage 참여방법_페이지4(ParticipateGuide participateGuide) {
        return ParticipateGuidePage.from(
                참여방법_페이지4_제목,
                참여방법_페이지4_미디어_제공_형식,
                참여방법_페이지4_미디어_url,
                참여방법_페이지4_요약,
                참여방법_페이지4_상세,
                참여방법_페이지4_추가,
                participateGuide);
    }

    public static ParticipateGuidePage 참여방법_페이지5(ParticipateGuide participateGuide) {
        return ParticipateGuidePage.from(
                참여방법_페이지5_제목,
                참여방법_페이지5_미디어_제공_형식,
                참여방법_페이지5_미디어_url,
                참여방법_페이지5_요약,
                참여방법_페이지5_상세,
                참여방법_페이지5_추가,
                participateGuide);
    }

    public static List<ParticipateGuidePage> 페이지_리스트_생성(ParticipateGuide participateGuide) {
        return List.of(
                참여방법_페이지1(participateGuide),
                참여방법_페이지2(participateGuide),
                참여방법_페이지3(participateGuide),
                참여방법_페이지4(participateGuide),
                참여방법_페이지5(participateGuide));
    }

    public static StampTourParticipateGuidePageReqDto 페이지_수정() {
        return new StampTourParticipateGuidePageReqDto(
                참여방법_수정_제목, 참여방법_수정_미디어_제공_형식, 참여방법_수정_미디어_url, 참여방법_수정_요약, 참여방법_수정_상세, 참여방법_수정_추가);
    }
}
