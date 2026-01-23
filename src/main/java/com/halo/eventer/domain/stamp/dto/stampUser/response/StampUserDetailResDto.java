package com.halo.eventer.domain.stamp.dto.stampUser.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampUserDetailResDto {
    private String name;
    private String phone;
    private String uuid;
    private String receivedPrizeName;
    private boolean finished;
    private List<UserMissionStatusResDto> missions;
    private String extraText;
    private int participantCount;
}
