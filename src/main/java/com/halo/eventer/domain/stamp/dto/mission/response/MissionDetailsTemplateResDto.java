package com.halo.eventer.domain.stamp.dto.mission.response;

import java.util.List;

import com.halo.eventer.domain.stamp.MissionDetailsTemplate;
import com.halo.eventer.domain.stamp.dto.stamp.enums.ButtonLayout;
import com.halo.eventer.domain.stamp.dto.stamp.enums.ExtraInfoLayout;
import com.halo.eventer.domain.stamp.dto.stamp.enums.MediaSpec;
import com.halo.eventer.domain.stamp.dto.stamp.enums.MissionDetailsDesignLayout;
import com.halo.eventer.domain.stamp.dto.stamp.response.ButtonResDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionDetailsTemplateResDto {
    private MissionDetailsDesignLayout missionDetailsDesignLayout;
    private String missionTitle;

    private boolean showSuccessCount;

    private MediaSpec mediaSpec;
    private String mediaUrl;

    private boolean showExtraInfos;
    private ExtraInfoLayout extraInfoType;
    private List<MissionExtraInfoSummaryResDto> extraInfos;

    private boolean showButtons;
    private ButtonLayout buttonLayout;
    private List<ButtonResDto> buttons;

    public static MissionDetailsTemplateResDto from(
            MissionDetailsTemplate template, boolean showTitle, boolean showSuccessCount, String missionName) {
        return new MissionDetailsTemplateResDto(
                template.getDesignLayout(),
                missionName,
                showSuccessCount,
                template.getMediaSpec(),
                template.getMediaUrl(),
                template.getShowExtraInfos(),
                template.getInfoLayout(),
                MissionExtraInfoSummaryResDto.fromEntities(template.getExtraInfo()),
                template.getShowButtons(),
                template.getButtonLayout(),
                ButtonResDto.fromEntities(template.getButtons()));
    }
}
