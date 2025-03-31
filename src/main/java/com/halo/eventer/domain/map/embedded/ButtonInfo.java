package com.halo.eventer.domain.map.embedded;

import com.halo.eventer.domain.map.dto.map.ButtonInfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
public class ButtonInfo {
    private String name;
    private String url;
    private String image;

    @Builder
    private ButtonInfo(String name, String url, String image) {
        this.name = name;
        this.url = url;
        this.image = image;
    }

    public static ButtonInfo from(ButtonInfoDto buttonInfoDto) {
        return ButtonInfo.builder()
                .name(buttonInfoDto.getName())
                .url(buttonInfoDto.getUrl())
                .image(buttonInfoDto.getImage())
                .build();
    }
}