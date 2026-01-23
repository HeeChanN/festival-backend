package com.halo.eventer.domain.stamp.dto.mission.response;

import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.UserMission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MissionIconRes {
    private long missionId;
    private String title;
    private boolean clear;
    private String imageUrl;

    public static MissionIconRes from(UserMission um) {
        Mission m = um.getMission();
        String url = um.getComplete() ? m.getClearedThumbnail() : m.getNotClearedThumbnail();
        return new MissionIconRes(m.getId(), m.getTitle(), um.getComplete(), url);
    }
}
