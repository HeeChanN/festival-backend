package com.halo.eventer.domain.stamp.dto.mission.response;

import java.util.List;

import com.halo.eventer.domain.stamp.MissionExtraInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionExtraInfoSummaryResDto {
    private long extraInfoId;
    private String titleText;
    private String bodyText;

    public static MissionExtraInfoSummaryResDto from(MissionExtraInfo e) {
        return new MissionExtraInfoSummaryResDto(e.getId(), e.getTitleText(), e.getBodyText());
    }

    public static List<MissionExtraInfoSummaryResDto> fromEntities(List<MissionExtraInfo> list) {
        return list.stream().map(MissionExtraInfoSummaryResDto::from).toList();
    }
}
