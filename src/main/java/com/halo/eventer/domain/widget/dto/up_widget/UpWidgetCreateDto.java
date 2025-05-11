package com.halo.eventer.domain.widget.dto.up_widget;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpWidgetCreateDto {
    private String name;
    private String url;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;

    @Builder
    private UpWidgetCreateDto(String name, String url, LocalDateTime periodStart, LocalDateTime periodEnd) {
        this.name = name;
        this.url = url;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    public static UpWidgetCreateDto of(String name, String url, LocalDateTime periodStart, LocalDateTime periodEnd) {
        return UpWidgetCreateDto.builder()
                .name(name)
                .url(url)
                .periodStart(periodStart)
                .periodEnd(periodEnd)
                .build();
    }
}
