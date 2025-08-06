package com.halo.eventer.domain.map.dto.map;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DurationBindingDto {
    private List<Long> idsToAdd;
    private List<Long> idsToRemove;

    public DurationBindingDto(List<Long> idsToAdd, List<Long> idsToRemove) {
        this.idsToAdd = idsToAdd;
        this.idsToRemove = idsToRemove;
    }
}
