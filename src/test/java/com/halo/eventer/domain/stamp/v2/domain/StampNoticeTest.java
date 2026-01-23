package com.halo.eventer.domain.stamp.v2.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.StampNotice;
import com.halo.eventer.domain.stamp.v2.fixture.StampNoticeFixture;
import com.halo.eventer.domain.stamp.v2.fixture.StampTourFixture;

import static com.halo.eventer.domain.stamp.v2.fixture.StampNoticeFixture.공지1_개인정보;
import static com.halo.eventer.domain.stamp.v2.fixture.StampNoticeFixture.공지1_주의사항;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class StampNoticeTest {

    private Stamp 스탬프투어1;
    private Festival 제주도_축제;
    private StampNotice 안내사항;

    @BeforeEach
    public void setUp() {
        제주도_축제 = FestivalFixture.축제_엔티티();
        스탬프투어1 = StampTourFixture.스탬프투어1_생성(제주도_축제);
        안내사항 = StampNoticeFixture.공지1_생성(스탬프투어1);
    }

    @Test
    void 공지사항_생성_정상() {
        assertThat(안내사항.getCautionContent()).isEqualTo(공지1_주의사항);
        assertThat(안내사항.getPersonalInformationContent()).isEqualTo(공지1_개인정보);
    }

    @Test
    void 공지사항_내용_수정() {
        String 바뀐_주의사항 = "변경된 공지 제목";
        String 바뀐_개인정보 = "변경된 내용입니다.";
        안내사항.update(바뀐_주의사항, 바뀐_개인정보);
        assertThat(안내사항.getCautionContent()).isEqualTo(바뀐_주의사항);
        assertThat(안내사항.getPersonalInformationContent()).isEqualTo(바뀐_개인정보);
    }
}
