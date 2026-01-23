package com.halo.eventer.domain.stamp.dto.stamp.response;

import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.domain.stamp.Stamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StampTourSummaryResDto {
    private Long stampId;
    private String title;
    private Boolean showStamp;

    public static StampTourSummaryResDto from(Stamp stamp) {
        return new StampTourSummaryResDto(stamp.getId(), stamp.getTitle(), stamp.getShowStamp());
    }

    public static List<StampTourSummaryResDto> fromEntities(List<Stamp> stamps) {
        return stamps.stream().map(StampTourSummaryResDto::from).collect(Collectors.toList());
    }
}
