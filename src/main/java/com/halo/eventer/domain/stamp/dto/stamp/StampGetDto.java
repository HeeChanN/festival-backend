package com.halo.eventer.domain.stamp.dto.stamp;

import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.domain.stamp.Stamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StampGetDto {
    private Long stampId;
    private boolean stampOn;
    private Integer stampFinishCnt;

    public static List<StampGetDto> fromEntities(List<Stamp> stampList) {
        return stampList.stream().map(StampGetDto::from).collect(Collectors.toList());
    }

    public static StampGetDto from(Stamp stamp) {
        return new StampGetDto(stamp.getId(), stamp.isActive(), stamp.getFinishCount());
    }
}
