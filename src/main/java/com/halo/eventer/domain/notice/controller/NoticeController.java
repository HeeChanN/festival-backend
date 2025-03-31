package com.halo.eventer.domain.notice.controller;

import com.halo.eventer.domain.notice.dto.*;
import com.halo.eventer.domain.notice.service.NoticeService;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.domain.notice.ArticleType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;


@Tag(name = "공지사항", description = "공지사항과 관련된 모든 것")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
@Validated
public class NoticeController {

  private final NoticeService noticeService;

  @PostMapping
  public NoticeResDto create(@RequestBody NoticeCreateDto noticeCreateDto,
                             @RequestParam("festivalId") Long id) {
    return noticeService.create(id, noticeCreateDto);
  }

  @GetMapping("/{noticeId}")
  public NoticeResDto getNotice(@PathVariable("noticeId") Long noticeId) {
    return noticeService.getNoticeById(noticeId);
  }

  @GetMapping()
  public PagedResponse<NoticeSummaryDto> inquireNoticeList(@RequestParam Long festivalId,
                                                           @RequestParam("type") ArticleType type,
                                                           @RequestParam(defaultValue="0") @Min(0) int page,
                                                           @RequestParam @Min(1) @Max(50) int size){
    return noticeService.getNoticesByType(festivalId, type,page,size);
  }

    @PostMapping("/banner")
    public NoticeResDto updateBanner(@RequestParam("noticeId") Long noticeId,
                                     @RequestParam("picked") Boolean picked) {
        return noticeService.updatePicked(noticeId,picked);
    }

    @GetMapping("/banner")
    public List<PickedNoticeResDto> getPickedBanner(@RequestParam("festivalId") Long festivalId){
        return noticeService.getPickedNotice(festivalId);
    }

    @PatchMapping("/banner")
    public List<PickedNoticeResDto> editDisplayOrder(@RequestBody List<PickedNoticeUpdateDto> noticeUpdateDtos){
        return noticeService.updateDisplayOrder(noticeUpdateDtos);
    }

    @PatchMapping("/{noticeId}")
    public NoticeResDto updateNotice(@PathVariable("noticeId") Long id,
                                     @RequestBody NoticeUpdateDto noticeUpdateDto) {
        return noticeService.updateNotice(id, noticeUpdateDto);
    }

    @DeleteMapping("/{noticeId}")
    public void deleteNotice(@PathVariable("noticeId") Long id) {
        noticeService.delete(id);
    }
}
