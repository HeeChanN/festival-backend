package com.halo.eventer.domain.notice.controller.admin;

import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.notice.ArticleType;
import com.halo.eventer.domain.notice.dto.*;
import com.halo.eventer.domain.notice.service.NoticeService;
import com.halo.eventer.global.common.page.PagedResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "공지사항 ADMIN 용", description = "백오피스용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notices")
public class AdminNoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public NoticeResDto create(
            @RequestBody NoticeCreateReqDto noticeCreateReqDto, @RequestParam("festivalId") Long id) {
        return noticeService.create(id, noticeCreateReqDto);
    }

    @GetMapping("/{noticeId}")
    public NoticeResDto getNotice(@PathVariable("noticeId") Long noticeId) {
        return noticeService.getNoticeById(noticeId);
    }

    @GetMapping()
    public PagedResponse<NoticeSummaryDto> inquireNoticeList(
            @RequestParam Long festivalId,
            @RequestParam("type") ArticleType type,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam @Min(1) @Max(50) int size) {
        return noticeService.getNoticesByType(festivalId, type, page, size);
    }

    @PostMapping("/banner")
    public NoticeResDto updateBanner(@RequestParam("noticeId") Long noticeId, @RequestParam("picked") Boolean picked) {
        return noticeService.updatePicked(noticeId, picked);
    }

    @GetMapping("/banner")
    public List<PickedNoticeResDto> getPickedBanner(@RequestParam("festivalId") Long festivalId) {
        return noticeService.getPickedNotice(festivalId);
    }

    @PatchMapping("/banner")
    public List<PickedNoticeResDto> editDisplayOrder(@RequestBody List<PickedNoticeUpdateDto> noticeUpdateDtos) {
        return noticeService.updateDisplayOrder(noticeUpdateDtos);
    }

    @PatchMapping("/{noticeId}")
    public NoticeResDto updateNotice(
            @PathVariable("noticeId") Long id, @RequestBody NoticeUpdateReqDto noticeUpdateReqDto) {
        return noticeService.updateNotice(id, noticeUpdateReqDto);
    }

    @DeleteMapping("/{noticeId}")
    public void deleteNotice(@PathVariable("noticeId") Long id) {
        noticeService.delete(id);
    }
}
