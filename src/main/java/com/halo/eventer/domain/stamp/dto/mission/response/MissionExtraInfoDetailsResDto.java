package com.halo.eventer.domain.stamp.dto.mission.response;

import com.halo.eventer.domain.stamp.MissionExtraInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionExtraInfoDetailsResDto {
    private long extraInfoId;
    private String titleText;
    private String bodyText;

    public static MissionExtraInfoDetailsResDto from(MissionExtraInfo e) {
        return new MissionExtraInfoDetailsResDto(e.getId(), e.getTitleText(), e.getBodyText());
    }
}
