package com.halo.eventer.domain.stamp.dto.mission.response;

import java.util.List;

import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.MissionDetailsTemplate;
import com.halo.eventer.domain.stamp.dto.stamp.enums.ButtonLayout;
import com.halo.eventer.domain.stamp.dto.stamp.enums.MediaSpec;
import com.halo.eventer.domain.stamp.dto.stamp.enums.MissionDetailsDesignLayout;
import com.halo.eventer.domain.stamp.dto.stamp.response.ButtonResDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MissionTemplateResDto {
    private Long missionId;
    private String title;

    private MediaSpec mediaSpec;
    private String mediaUrl;
    private MissionDetailsDesignLayout layout;

    private int userSuccessCount;
    private Integer requiredSuccessCount;
    private List<MissionExtraInfoSummaryResDto> extraInfos;

    private ButtonLayout buttonLayout;
    private List<ButtonResDto> buttons;

    public static MissionTemplateResDto from(Mission mission, MissionDetailsTemplate template, int userSuccessCount) {
        return new MissionTemplateResDto(
                mission.getId(),
                mission.getTitle(),
                template.getMediaSpec(),
                template.getMediaUrl(),
                template.getDesignLayout(),
                userSuccessCount,
                mission.getRequiredSuccessCount(),
                MissionExtraInfoSummaryResDto.fromEntities(template.getExtraInfo()),
                template.getButtonLayout(),
                ButtonResDto.fromEntities(template.getButtons()));
    }
}
