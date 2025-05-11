package com.halo.eventer.domain.image.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileDto {
    private String url;

    public FileDto(String url) {
        this.url = url;
    }
}
