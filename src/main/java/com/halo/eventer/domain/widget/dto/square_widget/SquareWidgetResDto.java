package com.halo.eventer.domain.widget.dto.square_widget;

import java.time.LocalDateTime;

import com.halo.eventer.domain.widget.entity.SquareWidget;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SquareWidgetResDto {
    private Long id;
    private String name;
    private String description;
    private String icon;
    private String url;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private SquareWidgetResDto(
            Long id,
            String name,
            String description,
            String icon,
            String url,
            Integer displayOrder,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.url = url;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SquareWidgetResDto from(SquareWidget squareWidget) {
        return SquareWidgetResDto.builder()
                .id(squareWidget.getId())
                .name(squareWidget.getName())
                .url(squareWidget.getUrl())
                .description(squareWidget.getDescriptionFeature().getDescription())
                .icon(squareWidget.getImageFeature().getImage())
                .displayOrder(squareWidget.getDisplayOrderFeature().getDisplayOrder())
                .createdAt(squareWidget.getCreatedAt())
                .updatedAt(squareWidget.getUpdatedAt())
                .build();
    }
}
