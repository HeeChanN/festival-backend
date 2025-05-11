package com.halo.eventer.domain.notice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.notice.ArticleType;
import com.halo.eventer.domain.notice.Notice;
import com.halo.eventer.domain.notice.NoticeFixture;
import com.halo.eventer.domain.notice.NoticeTestUtils;
import com.halo.eventer.domain.notice.dto.*;
import com.halo.eventer.domain.notice.dto.user.UserNoticeNoOffsetPageDto;
import com.halo.eventer.domain.notice.dto.user.UserNoticeResDto;
import com.halo.eventer.domain.notice.exception.NoticeNotFoundException;
import com.halo.eventer.domain.notice.repository.NoticeRepository;
import com.halo.eventer.global.common.page.PagedResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private FestivalRepository festivalRepository;

    @InjectMocks
    private NoticeService noticeService;

    private Festival festival;
    private NoticeCreateReqDto noticeCreateReqDto;
    private Notice notice;
    private NoticeUpdateReqDto noticeUpdateReqDto;
    private final Long festivalId = 1L;
    private final Long noticeId = 1L;

    @BeforeEach
    void setUp() {
        noticeCreateReqDto = NoticeFixture.공지사항_생성_DTO();
        noticeUpdateReqDto = NoticeFixture.공지사항_수정_DTO();
        festival = FestivalFixture.축제_엔티티();
        notice = Notice.from(festival, noticeCreateReqDto);
        setField(notice.getImages().get(0), "id", 1L);
        setField(notice, "id", noticeId);
        setField(notice, "createdAt", LocalDateTime.now());
        setField(notice, "updatedAt", LocalDateTime.now());
    }

    @Test
    void notice_생성_테스트() {
        // given
        given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
        given(noticeRepository.save(any())).willReturn(notice);

        // when
        NoticeResDto result = noticeService.create(festivalId, noticeCreateReqDto);

        // then
        NoticeTestUtils.assertNoticeDtoEqualsNotice(result, notice);
    }

    @Test
    void notice_생성시_festival_없을_경우_예외() {
        // given
        given(festivalRepository.findById(festivalId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> noticeService.create(festivalId, noticeCreateReqDto))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void noticeId_로단일조회() {
        // given
        given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));

        // when
        NoticeResDto result = noticeService.getNoticeById(noticeId);

        // then
        NoticeTestUtils.assertNoticeDtoEqualsNotice(result, notice);
    }

    @Test
    void 유저용_noticeId_로_단일조회() {
        // given
        given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));

        // when
        UserNoticeResDto result = noticeService.getNoticeByIdForUser(noticeId);

        // then
        NoticeTestUtils.assertUserNoticeDtoEqualsNotice(result, notice);
    }

    @Test
    void notice_단일조회시_없을_경우_예외() {
        // given
        given(noticeRepository.findById(noticeId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> noticeService.getNoticeById(noticeId)).isInstanceOf(NoticeNotFoundException.class);
    }

    @Test
    void notice_index_페이징_조회() {
        int size = 2;
        List<Notice> notices = new ArrayList<>(Arrays.asList(
                Notice.from(festival, noticeCreateReqDto),
                Notice.from(festival, noticeCreateReqDto),
                Notice.from(festival, noticeCreateReqDto)));
        given(noticeRepository.getNoticesNextPageByFestivalIdAndLastValue(
                        eq(festivalId),
                        eq(ArticleType.NOTICE.name()),
                        any(LocalDateTime.class),
                        anyLong(),
                        eq(size + 1)))
                .willReturn(notices);

        // when
        UserNoticeNoOffsetPageDto dto = noticeService.getNoticesByTypeWithNoOffsetPaging(
                festivalId, ArticleType.NOTICE, LocalDateTime.now(), 0L, size);

        // then
        assertThat(dto.getIsLast()).isFalse();
        assertThat(dto.getNotices()).hasSize(size);
    }

    @Test
    void notice_페이징_조회() {
        // given
        int page = 0, size = 2;
        Notice notice2 = Notice.from(festival, noticeCreateReqDto);
        setField(notice2, "id", 2L);
        List<Notice> notices = List.of(notice, notice2);
        Page<Notice> noticePage = new PageImpl<>(notices, PageRequest.of(page, size), 2L);
        given(noticeRepository.findAllByTypeAndFestivalIdOrderByUpdatedAtDesc(
                        ArticleType.NOTICE, festivalId, PageRequest.of(page, size)))
                .willReturn(noticePage);

        // when
        PagedResponse<NoticeSummaryDto> response =
                noticeService.getNoticesByType(festivalId, ArticleType.NOTICE, page, size);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(2);
        assertThat(response.getPageInfo().getPageNumber()).isEqualTo(page);
        assertThat(response.getPageInfo().getPageSize()).isEqualTo(size);
        assertThat(response.getPageInfo().getTotalElements()).isEqualTo(2);
        assertThat(response.getPageInfo().getTotalPages()).isEqualTo(1);
    }

    @Test
    void picked_수정_성공() {
        // given
        given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));

        // when
        NoticeResDto result = noticeService.updatePicked(noticeId, true);

        // then
        NoticeTestUtils.assertNoticeDtoEqualsNotice(result, notice);
    }

    @Test
    void notice_전체_수정_성공() {
        // given
        given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));
        setField(noticeUpdateReqDto, "deleteIds", List.of(1L));

        // when
        NoticeResDto result = noticeService.updateNotice(noticeId, noticeUpdateReqDto);

        // then
        NoticeTestUtils.assertNoticeDtoEqualsNotice(result, notice);
    }

    @Test
    void notice_삭제() {
        // when
        noticeService.delete(noticeId);

        // then
        verify(noticeRepository, times(1)).deleteById(noticeId);
    }

    @Test
    void picked_된_notice_리스트_조회() {
        // given
        Notice notice2 = Notice.from(festival, noticeCreateReqDto);
        setField(notice2, "id", 2L);
        notice.updatePicked(true);
        notice2.updatePicked(true);
        given(noticeRepository.findAllByPickedAndFestivalId(festivalId, true)).willReturn(List.of(notice, notice2));

        // when
        List<PickedNoticeResDto> results = noticeService.getPickedNotice(festivalId);

        // then
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0).getId()).isEqualTo(notice.getId());
        assertThat(results.get(1).getId()).isEqualTo(notice2.getId());
        assertThat(results.get(0).getDisplayOrder()).isEqualTo(11);
        assertThat(results.get(1).getDisplayOrder()).isEqualTo(11);
        assertThat(results.get(0).getThumbnail()).isEqualTo("thumbnail");
        assertThat(results.get(1).getThumbnail()).isEqualTo("thumbnail");
        assertThat(results.get(0).getType()).isEqualTo(ArticleType.NOTICE);
        assertThat(results.get(1).getType()).isEqualTo(ArticleType.NOTICE);
    }

    @Test
    void picked_notice_순서_변경() {
        Notice notice2 = Notice.from(festival, noticeCreateReqDto);
        setField(notice2, "id", 2L);
        notice.updatePicked(true);
        notice2.updatePicked(true);
        given(noticeRepository.findAllById(anySet())).willReturn(List.of(notice, notice2));
        PickedNoticeUpdateDto noticeUpdateDto1 = new PickedNoticeUpdateDto();
        PickedNoticeUpdateDto noticeUpdateDto2 = new PickedNoticeUpdateDto();
        setField(noticeUpdateDto1, "id", notice.getId());
        setField(noticeUpdateDto1, "displayOrder", 1);
        setField(noticeUpdateDto2, "id", notice2.getId());
        setField(noticeUpdateDto2, "displayOrder", 2);
        List<PickedNoticeUpdateDto> dtos = List.of(noticeUpdateDto1, noticeUpdateDto2);

        // when
        List<PickedNoticeResDto> result = noticeService.updateDisplayOrder(dtos);

        assertThat(result.get(0).getDisplayOrder()).isEqualTo(noticeUpdateDto1.getDisplayOrder());
        assertThat(result.get(1).getDisplayOrder()).isEqualTo(noticeUpdateDto2.getDisplayOrder());
    }
}
