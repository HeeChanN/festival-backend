package com.halo.eventer.domain.parking.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.image.dto.FileDto;
import com.halo.eventer.domain.parking.ParkingManagement;
import com.halo.eventer.domain.parking.dto.common.DisplayOrderChangeReqDto;
import com.halo.eventer.domain.parking.dto.parking_management.ParkingManagementReqDto;
import com.halo.eventer.domain.parking.dto.parking_management.ParkingManagementResDto;
import com.halo.eventer.domain.parking.dto.parking_management.ParkingManagementSubPageResDto;
import com.halo.eventer.domain.parking.dto.parking_management.ParkingSubPageReqDto;
import com.halo.eventer.domain.parking.enums.ParkingInfoType;
import com.halo.eventer.domain.parking.exception.ParkingManagementNotFoundException;
import com.halo.eventer.domain.parking.repository.ParkingManagementRepository;
import com.halo.eventer.global.constants.DisplayOrderConstants;
import com.halo.eventer.global.error.exception.BaseException;

import static com.halo.eventer.domain.festival.FestivalFixture.축제_엔티티;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ParkingManagementServiceTest {

    @InjectMocks
    private ParkingManagementService service;

    @Mock
    private ParkingManagementRepository parkingManagementRepository;

    @Mock
    private FestivalRepository festivalRepository;

    private Festival 축제;

    @BeforeEach
    void setUp() {
        축제 = 축제_엔티티();
    }

    @Test
    void 축제에_주차관리_생성_성공() {
        // given
        Long 축제_id = 1L;
        var 요청 = mock(ParkingManagementReqDto.class);
        String enumName = ParkingInfoType.values()[0].name();

        given(요청.getHeaderName()).willReturn("헤더");
        given(요청.getParkingInfoType()).willReturn(enumName);
        given(요청.getTitle()).willReturn("제목");
        given(요청.getDescription()).willReturn("설명");
        given(요청.getButtonName()).willReturn("버튼");
        given(요청.getButtonTargetUrl()).willReturn("url");
        given(요청.getBackgroundImage()).willReturn("bg.jpg");
        given(요청.isVisible()).willReturn(true);

        given(festivalRepository.findById(축제_id)).willReturn(Optional.of(축제));
        given(parkingManagementRepository.save(any(ParkingManagement.class))).willAnswer(inv -> inv.getArgument(0));

        // when
        service.create(축제_id, 요청);

        // then
        then(festivalRepository).should().findById(축제_id);
        then(parkingManagementRepository).should().save(any(ParkingManagement.class));

        assertThat(축제.getParkingManagements()).hasSize(1);
        ParkingManagement saved = 축제.getParkingManagements().get(0);
        assertThat(saved.getHeaderName()).isEqualTo("헤더");
        assertThat(saved.getParkingInfoType()).isEqualTo(ParkingInfoType.valueOf(enumName));
        assertThat(saved.getTitle()).isEqualTo("제목");
        assertThat(saved.getDescription()).isEqualTo("설명");
        assertThat(saved.getButtonName()).isEqualTo("버튼");
        assertThat(saved.getButtonTargetUrl()).isEqualTo("url");
        assertThat(saved.getBackgroundImage()).isEqualTo("bg.jpg");
        assertThat(saved.isVisible()).isTrue();
        assertThat(saved.getFestival()).isEqualTo(축제);
    }

    @Test
    void 축제에_주차관리_생성_실패_축제없음() {
        given(festivalRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(1L, mock(ParkingManagementReqDto.class)))
                .isInstanceOf(FestivalNotFoundException.class);

        then(parkingManagementRepository).should(never()).save(any());
    }

    @Test
    void 주차관리_단건_조회_성공() {
        // given
        ParkingManagement pm =
                ParkingManagement.of(축제, "헤더", ParkingInfoType.values()[0], "제목", "설명", "버튼", "url", "bg", true);
        given(parkingManagementRepository.findById(1L)).willReturn(Optional.of(pm));

        // when
        ParkingManagementResDto res = service.getParkingManagement(1L);

        // then
        assertThat(res.getHeaderName()).isEqualTo(pm.getHeaderName());
        assertThat(res.getParkingInfoType()).isEqualTo(pm.getParkingInfoType().name());
        assertThat(res.getTitle()).isEqualTo(pm.getTitle());
        assertThat(res.getDescription()).isEqualTo(pm.getDescription());
        assertThat(res.getButtonName()).isEqualTo(pm.getButtonName());
        assertThat(res.getButtonTargetUrl()).isEqualTo(pm.getButtonTargetUrl());
        assertThat(res.getBackgroundImage()).isEqualTo(pm.getBackgroundImage());
        assertThat(res.isVisible()).isEqualTo(pm.isVisible());
    }

    @Test
    void 주차관리_단건_조회_실패_없음() {
        given(parkingManagementRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getParkingManagement(1L))
                .isInstanceOf(ParkingManagementNotFoundException.class);
    }

    @Test
    void 주차관리_기본정보_수정_성공() {
        // given
        ParkingManagement pm =
                ParkingManagement.of(축제, "헤더", ParkingInfoType.values()[0], "제목", "설명", "버튼", "url", "bg", true);
        given(parkingManagementRepository.findById(1L)).willReturn(Optional.of(pm));

        var 요청 = mock(ParkingManagementReqDto.class);
        given(요청.getHeaderName()).willReturn("새헤더");
        given(요청.getParkingInfoType()).willReturn(ParkingInfoType.values()[0].name());
        given(요청.getTitle()).willReturn("새제목");
        given(요청.getDescription()).willReturn("새설명");
        given(요청.getButtonName()).willReturn("새버튼");
        given(요청.getButtonTargetUrl()).willReturn("새url");
        given(요청.getBackgroundImage()).willReturn("새bg");
        given(요청.isVisible()).willReturn(false);

        // when
        service.update(1L, 요청);

        // then
        assertThat(pm.getHeaderName()).isEqualTo("새헤더");
        assertThat(pm.getTitle()).isEqualTo("새제목");
        assertThat(pm.getDescription()).isEqualTo("새설명");
        assertThat(pm.getButtonName()).isEqualTo("새버튼");
        assertThat(pm.getButtonTargetUrl()).isEqualTo("새url");
        assertThat(pm.getBackgroundImage()).isEqualTo("새bg");
        assertThat(pm.isVisible()).isFalse();
    }

    @Test
    void 주차관리_기본정보_수정_실패_없음() {
        given(parkingManagementRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(1L, mock(ParkingManagementReqDto.class)))
                .isInstanceOf(ParkingManagementNotFoundException.class);
    }

    @Nested
    class 서브페이지_및_이미지 {

        @Test
        void 서브페이지_조회_성공() {
            ParkingManagement pm =
                    ParkingManagement.of(축제, "헤더", ParkingInfoType.values()[0], "제목", "설명", "버튼", "url", "bg", true);
            pm.updateSubPageHeaderName("서브");
            pm.addImage("a.jpg");
            pm.addImage("b.jpg");
            given(parkingManagementRepository.findByIdWithImages(1L)).willReturn(Optional.of(pm));

            ParkingManagementSubPageResDto res = service.getParkingManagementSubPage(1L);

            assertThat(res.getSubPageHeaderName()).isEqualTo("서브");
            assertThat(res.getImages()).hasSize(2);
        }

        @Test
        void 서브페이지_조회_실패_없음() {
            given(parkingManagementRepository.findByIdWithImages(anyLong())).willReturn(Optional.empty());

            assertThatThrownBy(() -> service.getParkingManagementSubPage(1L))
                    .isInstanceOf(ParkingManagementNotFoundException.class);
        }

        @Test
        void 서브페이지_헤더명_수정_성공() {
            ParkingManagement pm =
                    ParkingManagement.of(축제, "헤더", ParkingInfoType.values()[0], "제목", "설명", "버튼", "url", "bg", true);
            given(parkingManagementRepository.findById(1L)).willReturn(Optional.of(pm));

            var 요청 = mock(ParkingSubPageReqDto.class);
            given(요청.getSubPageHeaderName()).willReturn("새서브");

            service.updateSubPageHeaderName(1L, 요청);

            assertThat(pm.getSubPageHeaderName()).isEqualTo("새서브");
        }

        @Test
        void 주차맵_이미지_생성_성공() {
            ParkingManagement pm =
                    ParkingManagement.of(축제, "헤더", ParkingInfoType.values()[0], "제목", "설명", "버튼", "url", "bg", true);
            given(parkingManagementRepository.findById(1L)).willReturn(Optional.of(pm));

            var 파일 = mock(FileDto.class);
            given(파일.getUrl()).willReturn("map.jpg");

            service.createParkingMapImage(1L, 파일);

            assertThat(pm.getImages()).hasSize(1);
            assertThat(pm.getImages().get(0).getParkingManagement()).isEqualTo(pm);
        }

        @Test
        void 주차맵_이미지_생성_실패_상한초과() {
            ParkingManagement pm =
                    ParkingManagement.of(축제, "헤더", ParkingInfoType.values()[0], "제목", "설명", "버튼", "url", "bg", true);
            int limit = DisplayOrderConstants.DISPLAY_ORDER_LIMIT_VALUE;
            for (int i = 0; i < limit; i++) pm.addImage("img-" + i);

            given(parkingManagementRepository.findById(1L)).willReturn(Optional.of(pm));

            var 파일 = mock(FileDto.class);
            given(파일.getUrl()).willReturn("overflow.jpg");

            assertThatThrownBy(() -> service.createParkingMapImage(1L, 파일)).isInstanceOf(BaseException.class);
        }

        @Test
        void 이미지_표시순서_변경_성공() {
            ParkingManagement pm =
                    ParkingManagement.of(축제, "헤더", ParkingInfoType.values()[0], "제목", "설명", "버튼", "url", "bg", true);
            pm.addImage("1.jpg");
            pm.addImage("2.jpg");
            pm.addImage("3.jpg");
            ReflectionTestUtils.setField(pm.getImages().get(0), "id", 10L);
            ReflectionTestUtils.setField(pm.getImages().get(1), "id", 20L);
            ReflectionTestUtils.setField(pm.getImages().get(2), "id", 30L);

            given(parkingManagementRepository.findByIdWithImages(1L)).willReturn(Optional.of(pm));

            var 요청 = mock(DisplayOrderChangeReqDto.class);
            given(요청.getIds()).willReturn(List.of(30L, 10L, 20L));

            service.updateParkingMapImageDisplayOrder(1L, 요청);

            assertThat(pm.getImages())
                    .extracting(img -> (Long) ReflectionTestUtils.getField(img, "id"))
                    .containsExactly(30L, 10L, 20L);
        }

        @Test
        void 이미지_표시순서_변경_실패_주차관리없음() {
            given(parkingManagementRepository.findByIdWithImages(anyLong())).willReturn(Optional.empty());

            assertThatThrownBy(
                            () -> service.updateParkingMapImageDisplayOrder(1L, mock(DisplayOrderChangeReqDto.class)))
                    .isInstanceOf(ParkingManagementNotFoundException.class);
        }

        @Test
        void 이미지_표시순서_변경_실패_검증오류() {
            ParkingManagement pm =
                    ParkingManagement.of(축제, "헤더", ParkingInfoType.values()[0], "제목", "설명", "버튼", "url", "bg", true);
            pm.addImage("1.jpg");
            pm.addImage("2.jpg");
            ReflectionTestUtils.setField(pm.getImages().get(0), "id", 1L);
            ReflectionTestUtils.setField(pm.getImages().get(1), "id", 2L);

            given(parkingManagementRepository.findByIdWithImages(1L)).willReturn(Optional.of(pm));

            var 요청 = mock(DisplayOrderChangeReqDto.class);
            given(요청.getIds()).willReturn(List.of(1L)); // 크기 불일치

            assertThatThrownBy(() -> service.updateParkingMapImageDisplayOrder(1L, 요청))
                    .isInstanceOf(BaseException.class);
        }

        @Test
        void 이미지_삭제_성공() {
            ParkingManagement pm =
                    ParkingManagement.of(축제, "헤더", ParkingInfoType.values()[0], "제목", "설명", "버튼", "url", "bg", true);
            pm.addImage("1.jpg");
            pm.addImage("2.jpg");
            pm.addImage("3.jpg");
            ReflectionTestUtils.setField(pm.getImages().get(0), "id", 10L);
            ReflectionTestUtils.setField(pm.getImages().get(1), "id", 20L);
            ReflectionTestUtils.setField(pm.getImages().get(2), "id", 30L);

            given(parkingManagementRepository.findByIdWithImages(1L)).willReturn(Optional.of(pm));

            var 요청 = mock(DisplayOrderChangeReqDto.class);
            given(요청.getIds()).willReturn(List.of(10L, 30L));

            service.deleteParkingMapImages(1L, 요청);

            assertThat(pm.getImages())
                    .extracting(img -> (Long) ReflectionTestUtils.getField(img, "id"))
                    .containsExactly(20L);
        }

        @Test
        void 이미지_삭제_실패_주차관리없음() {
            given(parkingManagementRepository.findByIdWithImages(anyLong())).willReturn(Optional.empty());

            assertThatThrownBy(() -> service.deleteParkingMapImages(1L, mock(DisplayOrderChangeReqDto.class)))
                    .isInstanceOf(ParkingManagementNotFoundException.class);
        }
    }
}
