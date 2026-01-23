package com.halo.eventer.domain.stamp.dto.stampUser.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserMissionStatusResDto {
    private Long userMissionId;
    private Long missionId;
    private String missionTitle;
    private boolean clear;
}
