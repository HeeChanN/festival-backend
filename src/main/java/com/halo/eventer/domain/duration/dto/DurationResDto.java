package com.halo.eventer.domain.duration.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.domain.duration.Duration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class DurationResDto {

    private Long durationId;
    private LocalDate date;
    private Integer dayNumber;

    public DurationResDto(Duration duration) {
        this.durationId = duration.getId();
        this.date = duration.getDate();
        this.dayNumber = duration.getDayNumber();
    }

    public DurationResDto(Long durationId, LocalDate date, Integer dayNumber) {
        this.durationId = durationId;
        this.date = date;
        this.dayNumber = dayNumber;
    }

    public static List<DurationResDto> fromDurations(List<Duration> durations) {
        return durations.stream().map(DurationResDto::new).collect(Collectors.toList());
    }
}
