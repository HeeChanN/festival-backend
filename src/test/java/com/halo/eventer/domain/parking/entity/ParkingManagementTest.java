package com.halo.eventer.domain.parking.entity;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.image.Image;
import com.halo.eventer.domain.parking.ParkingManagement;
import com.halo.eventer.domain.parking.enums.ParkingInfoType;
import com.halo.eventer.global.constants.DisplayOrderConstants;
import com.halo.eventer.global.error.exception.BaseException;

import static com.halo.eventer.domain.festival.FestivalFixture.축제_엔티티;
import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
class ParkingManagementTest {

    private Festival 축제;

    @BeforeEach
    void setUp() {
        축제 = 축제_엔티티();
    }

    @Test
    void 정적팩토리_of_생성시_축제와_양방향_연결된다() {
        // given
        String header = "주차안내";
        ParkingInfoType infoType = ParkingInfoType.BASIC;
        String title = "주차장 이용 방법";
        String description = "설명";
        String buttonName = "자세히";
        String buttonUrl = "https://example.com";
        String bg = "bg.jpg";
        boolean visible = true;

        // when
        ParkingManagement pm =
                ParkingManagement.of(축제, header, infoType, title, description, buttonName, buttonUrl, bg, visible);

        // then
        assertThat(pm.getFestival()).isEqualTo(축제);
        assertThat(축제.getParkingManagements()).contains(pm);

        assertThat(pm.getHeaderName()).isEqualTo(header);
        assertThat(pm.getParkingInfoType()).isEqualTo(infoType);
        assertThat(pm.getTitle()).isEqualTo(title);
        assertThat(pm.getDescription()).isEqualTo(description);
        assertThat(pm.getButtonName()).isEqualTo(buttonName);
        assertThat(pm.getButtonTargetUrl()).isEqualTo(buttonUrl);
        assertThat(pm.getBackgroundImage()).isEqualTo(bg);
        assertThat(pm.isVisible()).isTrue();
    }

    @Test
    void 기본정보_update_호출시_모든_필드가_갱신된다() {
        // given: 초기 엔티티
        ParkingManagement pm = ParkingManagement.of(축제, "헤더", null, "제목", "설명", "버튼", "url", "bg", true);

        // when
        pm.update("변경헤더", null, "변경제목", "변경설명", "변경버튼", "변경url", "변경bg", false);

        // then
        assertThat(pm.getHeaderName()).isEqualTo("변경헤더");
        assertThat(pm.getParkingInfoType()).isNull();
        assertThat(pm.getTitle()).isEqualTo("변경제목");
        assertThat(pm.getDescription()).isEqualTo("변경설명");
        assertThat(pm.getButtonName()).isEqualTo("변경버튼");
        assertThat(pm.getButtonTargetUrl()).isEqualTo("변경url");
        assertThat(pm.getBackgroundImage()).isEqualTo("변경bg");
        assertThat(pm.isVisible()).isFalse();
    }

    @Test
    void 서브페이지_헤더명_updateSubPageHeaderName_갱신된다() {
        // given
        ParkingManagement pm = ParkingManagement.of(축제, "헤더", null, "제목", null, "버튼", null, null, true);

        // when
        pm.updateSubPageHeaderName("서브헤더");

        // then
        assertThat(pm.getSubPageHeaderName()).isEqualTo("서브헤더");
    }

    @Nested
    class 이미지_추가_제거 {

        ParkingManagement pm;

        @BeforeEach
        void init() {
            pm = ParkingManagement.of(축제, "헤더", null, "제목", null, "버튼", null, null, true);
        }

        @Test
        void addImage_호출시_이미지_목록에_추가된다() {
            // when
            pm.addImage("img1.jpg");
            pm.addImage("img2.jpg");

            // then
            assertThat(pm.getImages()).hasSize(2);
            assertThat(pm.getImages()).extracting("parkingManagement").containsOnly(pm);
        }

        @Test
        void 이미지_개수_상한_초과시_MAX_COUNT_EXCEEDED_예외() {
            // given
            int limit = DisplayOrderConstants.DISPLAY_ORDER_LIMIT_VALUE;
            for (int i = 0; i < limit; i++) {
                pm.addImage("img-" + i + ".jpg");
            }
            assertThat(pm.getImages()).hasSize(limit);

            // when // then
            assertThatThrownBy(() -> pm.addImage("overflow.jpg")).isInstanceOf(BaseException.class);
        }

        @Test
        void removeImages_호출시_ID에_해당하는_이미지가_제거된다() {
            pm.addImage("a.jpg");
            pm.addImage("b.jpg");
            pm.addImage("c.jpg");
            setImageIds(pm, 10L, 20L, 30L);

            pm.removeImages(List.of(10L, 30L));

            assertThat(pm.getImages()).hasSize(1);
            assertThat(pm.getImages()).extracting(Image::getId).containsExactly(20L);
        }
    }

    @Nested
    class 이미지_순서_변경 {

        ParkingManagement pm;

        @BeforeEach
        void init() {
            pm = ParkingManagement.of(축제, "헤더", null, "제목", null, "버튼", null, null, true);
            pm.addImage("1.jpg");
            pm.addImage("2.jpg");
            pm.addImage("3.jpg");
            // 재정렬 검증을 위해 이미지 ID를 세팅한다 (JPA 미사용 환경)
            setImageIds(pm, 1L, 2L, 3L);
        }

        @Test
        void reorderImages_성공시_리스트_순서가_ID_순서대로_변경된다() {
            // when
            pm.reorderImages(List.of(3L, 1L, 2L));

            // then
            assertThat(pm.getImages()).extracting(Image::getId).containsExactly(3L, 1L, 2L);
        }

        @Test
        void 재정렬_ID_중복이_있거나_크기_불일치면_INVALID_INPUT_VALUE() {
            // 크기 불일치
            assertThatThrownBy(() -> pm.reorderImages(List.of(1L, 2L))).isInstanceOf(BaseException.class);

            // 중복
            assertThatThrownBy(() -> pm.reorderImages(List.of(1L, 1L, 2L))).isInstanceOf(BaseException.class);
        }

        @Test
        void 재정렬_ID에_없는_ID가_포함되면_INVALID_INPUT_VALUE() {
            assertThatThrownBy(() -> pm.reorderImages(List.of(999L, 1L, 2L))).isInstanceOf(BaseException.class);
        }
    }

    /**
     * 테스트 편의를 위한 유틸:
     * JPA 없이 도메인 단위 테스트에서 Image id를 강제로 부여해 재정렬/삭제 로직을 검증한다.
     */
    private static void setImageIds(ParkingManagement pm, Long... ids) {
        var images = new ArrayList<>(pm.getImages());
        assertThat(images.size()).isEqualTo(ids.length);
        for (int i = 0; i < ids.length; i++) {
            ReflectionTestUtils.setField(images.get(i), "id", ids[i]);
        }
    }
}
