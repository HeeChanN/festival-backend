package com.halo.eventer.domain.stamp.v2.fixture;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.dto.stamp.enums.AuthMethod;

public class StampTourFixture {

    public static Long 존재하지_않는_스탬프 = 999L;

    public static Long 스탬프투어1_ID = 1L;
    public static String 스탬프투어1_제목 = "스탬프1 제목";
    public static boolean 스탬프투어1_활성화 = true;
    public static int 스탬프투어1_완료개수 = 5;
    public static String 스탬프투어1_관리자비번 = "password1";
    public static AuthMethod 스탬프투어1_유저인증방법 = AuthMethod.TAG_SCAN;

    public static Long 스탬프투어2 = 2L;
    public static String 스탬프투어2_제목 = "스탬프2 제목";
    public static boolean 스탬프투어2_활성화 = true;
    public static int 스탬프투어2_완료개수 = 3;
    public static String 스탬프투어2_관리자비번 = "password2";
    public static AuthMethod 스탬프투어2_유저인증방법 = AuthMethod.USER_CODE_PRESENT;

    public static String 바뀐_스탬프투어_제목 = "바뀐 제목";
    public static boolean 바뀐_스탬프투어_활성화 = false;
    public static int 바뀐_스탬프투어_완료개수 = 100;
    public static String 바뀐_스탬프투어_관리자비번 = "pw";
    public static AuthMethod 바뀐_스탬프투어_유저인증방법 = AuthMethod.TAG_SCAN;

    public static Stamp 스탬프투어1_생성(Festival festival) {
        return Stamp.createWith(festival, 스탬프투어1_제목, true);
    }

    public static Stamp 스탬프투어2_생성(Festival festival) {
        return Stamp.createWith(festival, 스탬프투어2_제목, true);
    }
}
