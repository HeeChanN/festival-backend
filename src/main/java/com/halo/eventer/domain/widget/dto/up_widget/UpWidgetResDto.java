package com.halo.eventer.domain.widget.dto.up_widget;

import java.time.LocalDateTime;

import com.halo.eventer.domain.widget.entity.UpWidget;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpWidgetResDto {

    private Long id;
    private String name;
    private String url;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private UpWidgetResDto(
            Long id,
            String name,
            String url,
            LocalDateTime periodStart,
            LocalDateTime periodEnd,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UpWidgetResDto from(UpWidget upWidget) {
        return UpWidgetResDto.builder()
                .id(upWidget.getId())
                .name(upWidget.getName())
                .url(upWidget.getUrl())
                .periodStart(upWidget.getPeriodFeature().getPeriodStart())
                .periodEnd(upWidget.getPeriodFeature().getPeriodEnd())
                .createdAt(upWidget.getCreatedAt())
                .updatedAt(upWidget.getUpdatedAt())
                .build();
    }
}
