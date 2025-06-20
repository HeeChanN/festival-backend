package com.halo.eventer.domain.lost_item.dto;

import java.time.LocalDate;

import com.halo.eventer.domain.lost_item.LostItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LostItemResDto {
    private Long id;
    private String name;
    private String description;
    private String thumbnail;
    private LocalDate findDate;

    @Builder
    private LostItemResDto(Long id, String name, String description, String thumbnail, LocalDate findDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.findDate = findDate;
    }

    public static LostItemResDto from(LostItem lostItem) {
        return LostItemResDto.builder()
                .id(lostItem.getId())
                .name(lostItem.getName())
                .description(lostItem.getDescription())
                .thumbnail(lostItem.getThumbnail())
                .findDate(lostItem.getFindDate())
                .build();
    }
}
