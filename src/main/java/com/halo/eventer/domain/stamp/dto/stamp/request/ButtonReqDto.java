package com.halo.eventer.domain.stamp.dto.stamp.request;

import com.halo.eventer.domain.stamp.dto.stamp.enums.ButtonAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ButtonReqDto {
    private int sequenceIndex;

    private String iconImgUrl;

    private String content;

    private ButtonAction action;

    private String targetUrl;
}
