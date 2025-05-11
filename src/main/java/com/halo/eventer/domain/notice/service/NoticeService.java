package com.halo.eventer.domain.notice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.notice.ArticleType;
import com.halo.eventer.domain.notice.Notice;
import com.halo.eventer.domain.notice.dto.*;
import com.halo.eventer.domain.notice.dto.user.UserNoticeNoOffsetPageDto;
import com.halo.eventer.domain.notice.dto.user.UserNoticeResDto;
import com.halo.eventer.domain.notice.exception.NoticeNotFoundException;
import com.halo.eventer.domain.notice.repository.NoticeRepository;
import com.halo.eventer.global.common.page.PageInfo;
import com.halo.eventer.global.common.page.PagedResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final FestivalRepository festivalRepository;

    @Transactional
    public NoticeResDto create(Long festivalId, NoticeCreateReqDto noticeCreateReqDto) {
        Festival festival = loadFestivalOrThrow(festivalId);
        Notice notice = noticeRepository.save(Notice.from(festival, noticeCreateReqDto));
        return NoticeResDto.from(notice);
    }

    @Transactional(readOnly = true)
    public NoticeResDto getNoticeById(Long id) {
        Notice notice = loadNoticeOrThrow(id);
        return NoticeResDto.from(notice);
    }

    @Transactional(readOnly = true)
    public UserNoticeResDto getNoticeByIdForUser(Long id) {
        Notice notice = loadNoticeOrThrow(id);
        return UserNoticeResDto.from(notice);
    }

    // TODO : 리팩토링 필요 (sql 개선, 코드 리팩토링)
    @Transactional(readOnly = true)
    public PagedResponse<NoticeSummaryDto> getNoticesByType(Long festivalId, ArticleType type, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Notice> noticePage =
                noticeRepository.findAllByTypeAndFestivalIdOrderByUpdatedAtDesc(type, festivalId, pageRequest);
        return convertToPagedResponse(noticePage);
    }

    @Transactional(readOnly = true)
    public UserNoticeNoOffsetPageDto getNoticesByTypeWithNoOffsetPaging(
            Long festivalId, ArticleType type, LocalDateTime lastUpdatedAt, Long lastId, int size) {
        List<Notice> notices = noticeRepository.getNoticesNextPageByFestivalIdAndLastValue(
                festivalId, type.name(), lastUpdatedAt, lastId, size + 1);

        boolean isLast = notices.size() <= size;
        if (!isLast) notices.remove(notices.size() - 1);

        return UserNoticeNoOffsetPageDto.of(notices, isLast);
    }

    @Transactional
    public NoticeResDto updatePicked(Long noticeId, Boolean pick) {
        Notice notice = loadNoticeOrThrow(noticeId);
        notice.updatePicked(pick);
        return NoticeResDto.from(notice);
    }

    @Transactional
    public NoticeResDto updateNotice(Long noticeId, NoticeUpdateReqDto noticeUpdateReqDto) {
        Notice notice = loadNoticeOrThrow(noticeId);
        notice.updateNotice(noticeUpdateReqDto);
        return NoticeResDto.from(notice);
    }

    @Transactional
    public void delete(Long id) {
        noticeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PickedNoticeResDto> getPickedNotice(Long festivalId) {
        return noticeRepository.findAllByPickedAndFestivalId(festivalId, true).stream()
                .map(PickedNoticeResDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PickedNoticeResDto> updateDisplayOrder(List<PickedNoticeUpdateDto> noticeDtos) {
        Map<Long, Integer> noticeIdToRankMap = noticeDtos.stream()
                .collect(Collectors.toMap(PickedNoticeUpdateDto::getId, PickedNoticeUpdateDto::getDisplayOrder));
        List<Notice> notices = noticeRepository.findAllById(noticeIdToRankMap.keySet());

        Notice.reOrderPickedNotices(notices, noticeIdToRankMap);
        return PickedNoticeResDto.noticesToPikedNoticeResDtos(notices);
    }

    private Festival loadFestivalOrThrow(Long id) {
        return festivalRepository.findById(id).orElseThrow(() -> new FestivalNotFoundException(id));
    }

    private Notice loadNoticeOrThrow(Long id) {
        return noticeRepository.findById(id).orElseThrow(() -> new NoticeNotFoundException(id));
    }

    private PagedResponse<NoticeSummaryDto> convertToPagedResponse(Page<Notice> noticePage) {
        List<NoticeSummaryDto> dtos =
                noticePage.getContent().stream().map(NoticeSummaryDto::from).collect(Collectors.toList());

        PageInfo pageInfo = PageInfo.builder()
                .pageNumber(noticePage.getNumber())
                .pageSize(noticePage.getSize())
                .totalElements(noticePage.getTotalElements())
                .totalPages(noticePage.getTotalPages())
                .build();

        return PagedResponse.<NoticeSummaryDto>builder()
                .content(dtos)
                .pageInfo(pageInfo)
                .build();
    }
}
