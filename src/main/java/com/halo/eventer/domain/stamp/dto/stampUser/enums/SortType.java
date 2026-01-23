package com.halo.eventer.domain.stamp.dto.stampUser.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortType {
    NAME("name"),
    CREATED_AT("createdAt");

    private final String property;
}
