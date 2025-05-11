package com.halo.eventer.domain.notice.dto;

import java.time.LocalDateTime;

import com.halo.eventer.domain.notice.Notice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeSummaryDto {

    private Long id;
    private String title;
    private String tag;
    private String writer;
    private String thumbnail;
    private boolean picked;
    private Integer displayOrder;
    private LocalDateTime updatedAt;

    @Builder
    private NoticeSummaryDto(
            Long id,
            String title,
            String tag,
            String writer,
            String thumbnail,
            boolean picked,
            Integer displayOrder,
            LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.tag = tag;
        this.writer = writer;
        this.thumbnail = thumbnail;
        this.picked = picked;
        this.displayOrder = displayOrder;
        this.updatedAt = updatedAt;
    }

    public static NoticeSummaryDto from(Notice notice) {
        return NoticeSummaryDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .tag(notice.getTag())
                .writer(notice.getWriter())
                .thumbnail(notice.getThumbnail())
                .picked(notice.isPicked())
                .updatedAt(notice.getUpdatedAt())
                .displayOrder(notice.getDisplayOrder())
                .build();
    }
}
