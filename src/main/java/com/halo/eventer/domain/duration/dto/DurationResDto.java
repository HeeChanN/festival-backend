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
    private Integer day;

    public DurationResDto(Duration duration) {
        this.durationId = duration.getId();
        this.date = duration.getDate();
        this.day = duration.getDayNumber();
    }

    public static List<DurationResDto> fromDurations(List<Duration> durations) {
        return durations.stream().map(DurationResDto::new).collect(Collectors.toList());
    }
}
