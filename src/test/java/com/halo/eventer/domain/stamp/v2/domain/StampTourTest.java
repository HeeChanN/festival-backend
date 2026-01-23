package com.halo.eventer.domain.stamp.v2.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.exception.StampClosedException;
import com.halo.eventer.domain.stamp.v2.fixture.StampTourFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("NonAsciiCharacters")
public class StampTourTest {

    private Stamp 스탬프투어1;
    private Stamp 스탬프투어2;
    private Festival 제주도_축제;

    @BeforeEach
    public void setUp() {
        제주도_축제 = FestivalFixture.축제_엔티티();
        스탬프투어1 = StampTourFixture.스탬프투어1_생성(제주도_축제);
        스탬프투어2 = StampTourFixture.스탬프투어2_생성(제주도_축제);
    }

    @Test
    void 페스티벌_등록_성공() {
        assertThat(스탬프투어1.getFestival()).isEqualTo(제주도_축제);
        assertThat(제주도_축제.getStamps()).contains(스탬프투어1);

        assertThat(스탬프투어2.getFestival()).isEqualTo(제주도_축제);
        assertThat(제주도_축제.getStamps()).contains(스탬프투어2);
    }

    @Test
    void 활성화_전환_성공() {
        스탬프투어1.switchActivation();
        assertThat(스탬프투어1.getActive()).isFalse();
    }

    //    @Test
    //    void 기본_설정_변경_성공() {
    //        String 바뀐_제목 = "새로운 제목";
    //        boolean 비활성화 = false;
    //        AuthMethod 바뀐_인증방법 = AuthMethod.USER_CODE_PRESENT;
    //        String 바뀐_관리자_비밀번호 = "pw1234";
    //        스탬프투어1.changeBasicSettings(비활성화, 바뀐_제목, 바뀐_인증방법, 바뀐_관리자_비밀번호);
    //        assertThat(스탬프투어1.getTitle()).isEqualTo(바뀐_제목);
    //        assertThat(스탬프투어1.getActive()).isEqualTo(비활성화);
    //        assertThat(스탬프투어1.getAuthMethod()).isEqualTo(바뀐_인증방법);
    //        assertThat(스탬프투어1.getPrizeReceiptAuthPassword()).isEqualTo(바뀐_관리자_비밀번호);
    //    }

    @Test
    void 종료_검증_비활성_예외() {
        스탬프투어1.switchActivation();
        assertThrows(StampClosedException.class, 스탬프투어1::validateActivation);
    }

    //    @Test
    //    void 스탬프투어_안내사항_변경() {
    //        String 바뀐_주의사항 = "변경된 주의사항";
    //        String 바뀐_개인정보 = "변경된 개인정보 뭐시기";
    //
    //        스탬프투어1.upsertNotice(바뀐_주의사항, 바뀐_개인정보);
    //        assertThat(스탬프투어1.getNotice().getParticipationNotice()).isEqualTo(바뀐_주의사항);
    //        assertThat(스탬프투어1.getNotice().getPrivacyConsent()).isEqualTo(바뀐_개인정보);
    //    }
}
