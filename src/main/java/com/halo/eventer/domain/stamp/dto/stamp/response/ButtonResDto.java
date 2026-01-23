package com.halo.eventer.domain.stamp.dto.stamp.response;

import java.util.Comparator;
import java.util.List;

import com.halo.eventer.domain.stamp.Button;
import com.halo.eventer.domain.stamp.dto.stamp.enums.ButtonAction;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ButtonResDto {
    private int sequenceIndex;
    private String content;
    private String iconImgUrl;
    private ButtonAction action;
    private String targetUrl;

    public static ButtonResDto from(Button button) {
        return new ButtonResDto(
                button.getSequenceIndex(),
                button.getContent(),
                button.getIconImg(),
                button.getAction(),
                button.getTargetUrl());
    }

    public static List<ButtonResDto> fromEntities(List<Button> buttons) {
        if (buttons == null || buttons.isEmpty()) return List.of();
        return buttons.stream()
                .sorted(Comparator.comparingInt(Button::getSequenceIndex))
                .map(ButtonResDto::from)
                .toList();
    }
}
