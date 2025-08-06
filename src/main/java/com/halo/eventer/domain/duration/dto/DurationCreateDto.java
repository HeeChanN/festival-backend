package com.halo.eventer.domain.duration.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DurationCreateDto {

    @NotNull(message = "date는 필수 값입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Min(1)
    private int dayNumber;

    @Builder
    public DurationCreateDto(LocalDate date, int dayNumber) {
        this.date = date;
        this.dayNumber = dayNumber;
    }
}
