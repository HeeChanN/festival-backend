package com.halo.eventer.domain.map.dto.map;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class DurationBindingDto {
    private List<Long> idsToAdd;
    private List<Long> idsToRemove;
}
