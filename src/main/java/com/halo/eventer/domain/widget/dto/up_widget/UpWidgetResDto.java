package com.halo.eventer.domain.widget.dto.up_widget;

import java.time.LocalDateTime;

import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.properties.UpWidgetProperties;
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

    public static UpWidgetResDto from(Widget widget) {
        UpWidgetProperties props = widget.getTypedProperties(UpWidgetProperties.class);
        return UpWidgetResDto.builder()
                .id(widget.getId())
                .name(widget.getName())
                .url(widget.getUrl())
                .periodStart(props.getPeriodStart())
                .periodEnd(props.getPeriodEnd())
                .createdAt(widget.getCreatedAt())
                .updatedAt(widget.getUpdatedAt())
                .build();
    }

    public static UpWidgetResDto of(
            Long id,
            String name,
            String url,
            LocalDateTime periodStart,
            LocalDateTime periodEnd,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return new UpWidgetResDto(id, name, url, periodStart, periodEnd, createdAt, updatedAt);
    }
}
