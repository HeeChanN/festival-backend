package com.halo.eventer.global.common.sort;

public enum SortOption {
    CREATED_AT("createAt"),
    UPDATED_AT("updateAt");

    private final String fieldName;

    SortOption(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
