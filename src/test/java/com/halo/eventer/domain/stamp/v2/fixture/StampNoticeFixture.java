package com.halo.eventer.domain.stamp.v2.fixture;

import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.StampNotice;

public class StampNoticeFixture {

    public static Long 안내사항1_ID = 1L;
    public static String 공지1_주의사항 = "공지1";
    public static String 공지1_개인정보 = "오전 10시부터 오후 5시까지 운영됩니다.";

    public static Long 안내사항2_ID = 2L;
    public static String 공지2_주의사항 = "중요 공지";
    public static String 공지2_개인정보 = "마지막 날에는 조기 종료될 수 있습니다.";

    public static StampNotice 공지1_생성(Stamp stamp) {
        return StampNotice.from(stamp, 공지1_주의사항, 공지1_개인정보);
    }

    public static StampNotice 공지2_생성(Stamp stamp) {
        return StampNotice.from(stamp, 공지2_주의사항, 공지2_개인정보);
    }
}
