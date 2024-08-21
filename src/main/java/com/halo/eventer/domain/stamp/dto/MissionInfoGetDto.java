package com.halo.eventer.domain.stamp.dto;

import com.halo.eventer.domain.stamp.StampUser;
import lombok.Getter;

@Getter
public class MissionInfoGetDto {
    private boolean mission1;
    private boolean mission2;
    private boolean mission3;
    private boolean mission4;
    private boolean mission5;
    private boolean mission6;
    private boolean finished;

    public MissionInfoGetDto(StampUser stampUser) {
        this.mission1 = stampUser.isMission1();
        this.mission2 = stampUser.isMission2();
        this.mission3 = stampUser.isMission3();
        this.mission4 = stampUser.isMission4();
        this.mission5 = stampUser.isMission5();
        this.mission6 = stampUser.isMission6();
        this.finished = stampUser.isFinished();
    }
}
