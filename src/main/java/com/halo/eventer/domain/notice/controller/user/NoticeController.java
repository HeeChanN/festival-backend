package com.halo.eventer.domain.notice.controller.user;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.notice.ArticleType;
import com.halo.eventer.domain.notice.dto.user.UserNoticeNoOffsetPageDto;
import com.halo.eventer.domain.notice.dto.user.UserNoticePageReqDto;
import com.halo.eventer.domain.notice.dto.user.UserNoticeResDto;
import com.halo.eventer.domain.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "공지사항/이벤트", description = "홈페이지용 Notice API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/{noticeId}")
    public UserNoticeResDto getNoticeForUser(@PathVariable("noticeId") Long noticeId) {
        return noticeService.getNoticeByIdForUser(noticeId);
    }

    @GetMapping()
    public UserNoticeNoOffsetPageDto getNotices(
            @RequestParam("festivalId") Long festivalId,
            @RequestParam("type") ArticleType type,
            @Validated @ModelAttribute UserNoticePageReqDto userNoticePageReqDto) {
        return noticeService.getNoticesByTypeWithNoOffsetPaging(
                festivalId,
                type,
                userNoticePageReqDto.getLastUpdatedAt(),
                userNoticePageReqDto.getLastId(),
                userNoticePageReqDto.getSize());
    }
}
