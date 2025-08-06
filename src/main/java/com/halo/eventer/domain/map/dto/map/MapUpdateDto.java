package com.halo.eventer.domain.map.dto.map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MapUpdateDto {

    @NotNull
    @Size(min = 1, max = 20)
    private String name;

    private String summary;
    private String content;
    private String thumbnail;
    private String icon;

    @NotNull
    @Valid
    private OperationInfoDto operationInfo;

    @NotNull
    private LocationInfoDto locationInfo;

    @NotNull
    private ButtonInfoDto buttonInfo;

    private DurationBindingDto durationBinding;

    public MapUpdateDto(
            String name,
            String summary,
            String content,
            String thumbnail,
            String icon,
            OperationInfoDto operationInfo,
            LocationInfoDto locationInfo,
            ButtonInfoDto buttonInfo,
            DurationBindingDto durationBinding) {
        this.name = name;
        this.summary = summary;
        this.content = content;
        this.thumbnail = thumbnail;
        this.icon = icon;
        this.operationInfo = operationInfo;
        this.locationInfo = locationInfo;
        this.buttonInfo = buttonInfo;
        this.durationBinding = durationBinding;
    }
}
