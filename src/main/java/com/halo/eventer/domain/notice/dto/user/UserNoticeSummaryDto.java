package com.halo.eventer.domain.notice.dto.user;

import java.time.LocalDateTime;

import com.halo.eventer.domain.notice.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserNoticeSummaryDto {
    private Long id;
    private String title;
    private String tag;
    private String writer;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    @Builder
    private UserNoticeSummaryDto(
            Long id, String title, String tag, String writer, LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.tag = tag;
        this.writer = writer;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public static UserNoticeSummaryDto from(Notice notice) {
        return UserNoticeSummaryDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .tag(notice.getTag())
                .writer(notice.getWriter())
                .updatedAt(notice.getUpdatedAt())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
