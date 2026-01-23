package com.halo.eventer.domain.stamp.dto.stamp.request;

import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.halo.eventer.domain.stamp.dto.stamp.enums.ButtonLayout;
import com.halo.eventer.domain.stamp.dto.stamp.enums.MainPageDesignTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampTourMainPageReqDto {
    @NotNull
    private MainPageDesignTemplate mainPageDesignTemplate;

    @NotEmpty
    private String backgroundImgUrl;

    @NotNull
    private ButtonLayout buttonLayout;

    private List<ButtonReqDto> buttons;
}
