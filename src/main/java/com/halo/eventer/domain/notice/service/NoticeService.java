package com.halo.eventer.domain.notice.service;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.image.ImageRepository;
import com.halo.eventer.domain.notice.Notice;
import com.halo.eventer.domain.notice.dto.*;
import com.halo.eventer.domain.notice.exception.NoticeNotFoundException;
import com.halo.eventer.domain.notice.repository.NoticeRepository;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.domain.notice.ArticleType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.halo.eventer.global.common.page.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

  private final NoticeRepository noticeRepository;
  private final ImageRepository imageRepository;
  private final FestivalRepository festivalRepository;

  @Transactional
  public NoticeResDto create(Long festivalId, NoticeCreateDto noticeCreateDto) {
    Festival festival = festivalRepository.findById(festivalId)
            .orElseThrow(() -> new FestivalNotFoundException(festivalId));

    Notice notice = Notice.from(festival,noticeCreateDto);
    notice.addImages(noticeCreateDto.getImages());

    Notice savedNotice = noticeRepository.save(notice);
    return NoticeResDto.from(savedNotice);
  }

  @Transactional(readOnly = true)
  public NoticeResDto getNoticeById(Long id) {
    Notice notice = loadNoticeOrThrow(id);
    return NoticeResDto.from(notice);
  }

  @Transactional(readOnly = true)
  public PagedResponse<NoticeSummaryDto> getNoticesByType(Long festivalId, ArticleType type, int page, int size) {
    PageRequest pageRequest = PageRequest.of(page,size);
    Page<Notice> noticePage = noticeRepository
            .findAllByTypeAndFestivalIdOrderByUpdatedAtDesc(type,festivalId,pageRequest);

    return convertToPagedResponse(noticePage);
  }

  @Transactional
  public NoticeResDto updatePicked(Long noticeId, Boolean pick) {
    Notice notice = loadNoticeOrThrow(noticeId);
    notice.updatePicked(pick);
    return NoticeResDto.from(notice);
  }

  @Transactional
  public NoticeResDto updateNotice(Long noticeId, NoticeUpdateDto noticeUpdateDto) {
    Notice notice = loadNoticeOrThrow(noticeId);

    imageRepository.deleteByIdIn(noticeUpdateDto.getDeleteIds());
    notice.updateNotice(noticeUpdateDto);
    notice.addImages(noticeUpdateDto.getImages());
    return NoticeResDto.from(notice);
  }

  @Transactional
  public void delete(Long id) {
    noticeRepository.deleteById(id);
  }

  public List<PickedNoticeResDto> getPickedNotice(Long festivalId) {
    return noticeRepository.findAllByPickedAndFestivalId(festivalId,true).stream()
            .map(PickedNoticeResDto::new)
            .collect(Collectors.toList());
  }

  @Transactional
  public List<PickedNoticeResDto> updateDisplayOrder(List<PickedNoticeUpdateDto> noticeDtos) {
    Map<Long, Integer> noticeIdToRankMap = noticeDtos.stream().collect(
            Collectors.toMap(PickedNoticeUpdateDto::getId, PickedNoticeUpdateDto::getDisplayOrder));

    List<Notice> notices = noticeRepository.findAllById(noticeIdToRankMap.keySet());

    notices.forEach(notice -> {
      Integer rank = noticeIdToRankMap.get(notice.getId());
      notice.setRank(rank);
    });
    return PickedNoticeResDto.noticesToPikedNoticeResDtos(notices);
  }

  private Notice loadNoticeOrThrow(Long id) {
    return noticeRepository.findById(id).orElseThrow(() -> new NoticeNotFoundException(id));
  }

  private PagedResponse<NoticeSummaryDto> convertToPagedResponse(Page<Notice> noticePage) {
    List<NoticeSummaryDto> dtos = noticePage.getContent().stream()
            .map(NoticeSummaryDto::from)
            .collect(Collectors.toList());

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
