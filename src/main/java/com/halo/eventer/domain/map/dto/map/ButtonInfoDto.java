package com.halo.eventer.domain.map.dto.map;

import com.halo.eventer.domain.map.embedded.ButtonInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ButtonInfoDto {
    private String name;
    private String url;
    private String image;

    @Builder
    private ButtonInfoDto(String name, String url, String image) {
        this.name = name;
        this.url = url;
        this.image = image;
    }

    public static ButtonInfoDto from(ButtonInfo buttonInfo) {
        return ButtonInfoDto.builder()
                .name(buttonInfo.getName())
                .url(buttonInfo.getUrl())
                .image(buttonInfo.getImage())
                .build();
    }
}
