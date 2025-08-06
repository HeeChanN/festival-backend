package com.halo.eventer.domain.festival.dto;

import jakarta.validation.constraints.Pattern;

import com.halo.eventer.domain.festival.Festival;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ColorDto {

    private static final String HEX_COLOR_REGEX = "^#(?:[0-9A-Fa-f]{3}|[0-9A-Fa-f]{6})$";
    private static final String HEX_MESSAGE = "색상은 #RGB 또는 #RRGGBB 형식이어야 합니다.";

    @Pattern(regexp = HEX_COLOR_REGEX, message = HEX_MESSAGE)
    private String mainColor;

    @Pattern(regexp = HEX_COLOR_REGEX, message = HEX_MESSAGE)
    private String backgroundColor;

    @Pattern(regexp = HEX_COLOR_REGEX, message = HEX_MESSAGE)
    private String subColor;

    @Pattern(regexp = HEX_COLOR_REGEX, message = HEX_MESSAGE)
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
