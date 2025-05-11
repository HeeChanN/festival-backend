package com.halo.eventer.domain.festival.dto;

import com.halo.eventer.domain.festival.Festival;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ColorDto {
    private String mainColor;
    private String backgroundColor;
    private String subColor;
    private String fontColor;

    public ColorDto(
            final String mainColor, final String backgroundColor, final String subColor, final String fontColor) {
        this.mainColor = mainColor;
        this.backgroundColor = backgroundColor;
        this.subColor = subColor;
        this.fontColor = fontColor;
    }

    public static ColorDto from(final Festival festival) {
        return new ColorDto(
                festival.getMainColor(),
                festival.getBackgroundColor(),
                festival.getSubColor(),
                festival.getFontColor());
    }
}
