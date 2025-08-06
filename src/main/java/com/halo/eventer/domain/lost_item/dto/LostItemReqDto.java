package com.halo.eventer.domain.lost_item.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LostItemReqDto {

    @NotNull
    private String name;

    @Size(max = 255)
    private String description;

    @Size(max = 1000)
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
