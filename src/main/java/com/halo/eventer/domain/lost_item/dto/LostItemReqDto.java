package com.halo.eventer.domain.lost_item.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LostItemReqDto {
    private String name;
    private String description;
    private String thumbnail;
    private LocalDate findDate;

    @Builder
    public LostItemReqDto(String name, String description, String thumbnail, LocalDate findDate) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.findDate = findDate;
    }
}
