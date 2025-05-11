package com.halo.eventer.infra.sms.common;

public class MessageTemplate {
    public static String missingPersonTemplate = "< 실종자 등록 알림 안내 >\n"
            + "이름 : %s\n"
            + "연령대 : %s\n"
            + "성별 : %s\n"
            + "실종 위치 : %s\n\n"
            + "자세한 실종자 정보는 링크 확인\n"
            + "%s";
}
