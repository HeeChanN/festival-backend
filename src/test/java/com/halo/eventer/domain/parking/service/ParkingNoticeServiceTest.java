package com.halo.eventer.domain.parking.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.halo.eventer.domain.parking.ParkingManagement;
import com.halo.eventer.domain.parking.ParkingNotice;
import com.halo.eventer.domain.parking.dto.common.VisibilityChangeReqDto;
import com.halo.eventer.domain.parking.dto.parking_notice.ParkingNoticeContentReqDto;
import com.halo.eventer.domain.parking.dto.parking_notice.ParkingNoticeReqDto;
import com.halo.eventer.domain.parking.dto.parking_notice.ParkingNoticeResDto;
import com.halo.eventer.domain.parking.exception.ParkingManagementNotFoundException;
import com.halo.eventer.domain.parking.exception.ParkingNoticeNotFoundException;
import com.halo.eventer.domain.parking.repository.ParkingManagementRepository;
import com.halo.eventer.domain.parking.repository.ParkingNoticeRepository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ParkingNoticeServiceTest {

    @InjectMocks
    private ParkingNoticeService service;

    @Mock
    private ParkingNoticeRepository parkingNoticeRepository;

    @Mock
    private ParkingManagementRepository parkingManagementRepository;

    private ParkingManagement 주차관리;

    @BeforeEach
    void setUp() {
        // ParkingManagement는 외부 동작만 필요하므로 mock 사용
        주차관리 = mock(ParkingManagement.class);
    }

    @Test
    void 공지_생성_성공() {
        // given
        long 관리_id = 1L;
        given(parkingManagementRepository.findById(관리_id)).willReturn(Optional.of(주차관리));
        given(parkingNoticeRepository.save(any(ParkingNotice.class))).willAnswer(inv -> inv.getArgument(0));

        var 요청 = mock(ParkingNoticeReqDto.class);
        given(요청.getTitle()).willReturn("제목");
        given(요청.getContent()).willReturn("내용");

        // when
        service.create(관리_id, 요청);

        // then
        then(parkingManagementRepository).should().findById(관리_id);
        ArgumentCaptor<ParkingNotice> captor = ArgumentCaptor.forClass(ParkingNotice.class);
        then(parkingNoticeRepository).should().save(captor.capture());

        ParkingNotice saved = captor.getValue();
        assertThat(saved.getTitle()).isEqualTo("제목");
        assertThat(saved.getContent()).isEqualTo("내용");
        assertThat(saved.getVisible()).isTrue();
        assertThat(saved.getParkingManagement()).isEqualTo(주차관리);
    }

    @Test
    void 공지_생성_실패_주차관리없음() {
        // given
        given(parkingManagementRepository.findById(anyLong())).willReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> service.create(1L, mock(ParkingNoticeReqDto.class)))
                .isInstanceOf(ParkingManagementNotFoundException.class);

        then(parkingNoticeRepository).should(never()).save(any());
    }

    @Nested
    class 조회 {

        @Test
        void 공지_목록_조회_성공() {
            // given
            long 관리_id = 7L;
            var pm = mock(ParkingManagement.class);
            var n1 = ParkingNotice.of("t1", "c1", pm);
            var n2 = ParkingNotice.of("t2", "c2", pm);
            ReflectionTestUtils.setField(n1, "id", 10L);
            ReflectionTestUtils.setField(n2, "id", 20L);
            ReflectionTestUtils.setField(n2, "visible", false);

            given(parkingNoticeRepository.findByIdParkingManagementId(관리_id)).willReturn(List.of(n1, n2));

            // when
            List<ParkingNoticeResDto> res = service.getParkingNotices(관리_id);

            // then
            assertThat(res).hasSize(2);
            assertThat(res).extracting(ParkingNoticeResDto::getId).containsExactly(10L, 20L);
            assertThat(res).extracting(ParkingNoticeResDto::getTitle).containsExactly("t1", "t2");
            assertThat(res).extracting(ParkingNoticeResDto::getContent).containsExactly("c1", "c2");
            assertThat(res).extracting(ParkingNoticeResDto::getVisible).containsExactly(true, false);
        }

        @Test
        void 가시_공지_목록_조회_성공() {
            // given
            long 관리_id = 8L;
            var pm = mock(ParkingManagement.class);
            var n1 = ParkingNotice.of("t1", "c1", pm);
            var n2 = ParkingNotice.of("t2", "c2", pm);
            ReflectionTestUtils.setField(n1, "id", 11L);
            ReflectionTestUtils.setField(n2, "id", 22L);

            given(parkingNoticeRepository.findByIdParkingManagementIdAndVisible(관리_id, true))
                    .willReturn(List.of(n1, n2));

            // when
            List<ParkingNoticeResDto> res = service.getVisibleParkingNotices(관리_id);

            // then
            assertThat(res).hasSize(2);
            assertThat(res).allMatch(dto -> dto.getVisible().equals(true));
            assertThat(res).extracting(ParkingNoticeResDto::getId).containsExactly(11L, 22L);
        }
    }

    @Nested
    class 수정_및_삭제 {

        @Test
        void 내용_수정_성공() {
            // given
            var pm = mock(ParkingManagement.class);
            var notice = ParkingNotice.of("old-title", "old-content", pm);
            ReflectionTestUtils.setField(notice, "id", 100L);

            given(parkingNoticeRepository.findById(100L)).willReturn(Optional.of(notice));

            var 요청 = mock(ParkingNoticeContentReqDto.class);
            given(요청.getTitle()).willReturn("new-title");
            given(요청.getContent()).willReturn("new-content");

            // when
            service.updateContent(100L, 요청);

            // then
            assertThat(notice.getTitle()).isEqualTo("new-title");
            assertThat(notice.getContent()).isEqualTo("new-content");
        }

        @Test
        void 내용_수정_실패_공지없음() {
            given(parkingNoticeRepository.findById(anyLong())).willReturn(Optional.empty());

            assertThatThrownBy(() -> service.updateContent(1L, mock(ParkingNoticeContentReqDto.class)))
                    .isInstanceOf(ParkingNoticeNotFoundException.class);
        }

        @Test
        void 가시성_변경_성공() {
            // given
            var pm = mock(ParkingManagement.class);
            var notice = ParkingNotice.of("t", "c", pm);
            ReflectionTestUtils.setField(notice, "id", 200L);

            given(parkingNoticeRepository.findById(200L)).willReturn(Optional.of(notice));

            var 요청 = mock(VisibilityChangeReqDto.class);
            given(요청.getVisible()).willReturn(false);

            // when
            service.changeVisibility(200L, 요청);

            // then
            assertThat(notice.getVisible()).isFalse();
        }

        @Test
        void 가시성_변경_실패_공지없음() {
            given(parkingNoticeRepository.findById(anyLong())).willReturn(Optional.empty());

            assertThatThrownBy(() -> service.changeVisibility(1L, mock(VisibilityChangeReqDto.class)))
                    .isInstanceOf(ParkingNoticeNotFoundException.class);
        }

        @Test
        void 삭제_성공() {
            // given
            var pm = mock(ParkingManagement.class);
            var notice = ParkingNotice.of("t", "c", pm);
            ReflectionTestUtils.setField(notice, "id", 300L);

            given(parkingNoticeRepository.findById(300L)).willReturn(Optional.of(notice));

            // when
            service.delete(300L);

            // then
            then(parkingNoticeRepository).should().delete(notice);
        }

        @Test
        void 삭제_실패_공지없음() {
            given(parkingNoticeRepository.findById(anyLong())).willReturn(Optional.empty());

            assertThatThrownBy(() -> service.delete(1L)).isInstanceOf(ParkingNoticeNotFoundException.class);

            then(parkingNoticeRepository).should(never()).delete(any());
        }
    }
}
