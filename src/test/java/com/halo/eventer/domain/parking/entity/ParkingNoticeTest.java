package com.halo.eventer.domain.parking.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.parking.ParkingManagement;
import com.halo.eventer.domain.parking.ParkingNotice;

import static com.halo.eventer.domain.festival.FestivalFixture.축제_엔티티;
import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
class ParkingNoticeTest {

    private Festival 축제;
    private ParkingManagement 주차관리;

    @BeforeEach
    void setUp() {
        축제 = 축제_엔티티();
        주차관리 = ParkingManagement.of(축제, "주차 안내", null, "메인 제목", "설명", "자세히 보기", "https://example.com", "bg.jpg", true);
    }

    @Test
    void 정적팩토리_of_생성시_양방향_연결과_필드가_설정된다() {
        // given
        String title = "공지 제목";
        String content = "공지 내용";
        Boolean visible = true;

        // when
        ParkingNotice notice = ParkingNotice.of(title, content, 주차관리);

        // then
        assertThat(notice.getParkingManagement()).isEqualTo(주차관리);
        assertThat(주차관리.getParkingNotices()).contains(notice);

        assertThat(notice.getTitle()).isEqualTo(title);
        assertThat(notice.getContent()).isEqualTo(content);
        assertThat(notice.getVisible()).isTrue();
    }

    @Test
    void updateNotice_호출시_제목과_내용이_갱신된다() {
        // given
        ParkingNotice notice = ParkingNotice.of("초기 제목", "초기 내용", 주차관리);

        // when
        notice.updateNotice("변경 제목", "변경 내용");

        // then
        assertThat(notice.getTitle()).isEqualTo("변경 제목");
        assertThat(notice.getContent()).isEqualTo("변경 내용");
        // 가시성/연결은 변경되지 않음
        assertThat(notice.getVisible()).isTrue();
        assertThat(notice.getParkingManagement()).isEqualTo(주차관리);
    }

    @Nested
    class 가시성_변경 {

        ParkingNotice notice;

        @BeforeEach
        void init() {
            notice = ParkingNotice.of("제목", "내용", 주차관리);
        }

        @Test
        void changeVisible_true에서_false로_변경된다() {
            // when
            notice.changeVisible(false);

            // then
            assertThat(notice.getVisible()).isFalse();
        }

        @Test
        void changeVisible_false에서_true로_변경된다() {
            // given
            notice.changeVisible(false);

            // when
            notice.changeVisible(true);

            // then
            assertThat(notice.getVisible()).isTrue();
        }
    }
}
