package com.halo.eventer.domain.stamp.fixture;

import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.dto.stamp.MissionSetDto;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class MissionFixture {

    public static Mission 미션_엔티티_생성() {
        return Mission.from(미션_생성_DTO_생성());
    }

    public static MissionSetDto 미션_생성_DTO_생성() {
        MissionSetDto dto = new MissionSetDto();
        setField(dto, "boothId", 1L);
        setField(dto, "title", "미션 제목");
        setField(dto, "content", "미션 설명");
        setField(dto, "place", "A관 1층");
        setField(dto, "time", "10:00 ~ 18:00");
        setField(dto, "clearedThumbnail", "cleared.png");
        setField(dto, "notClearedThumbnail", "not_cleared.png");
        return dto;
    }

    public static MissionUpdateDto 미션_업데이트_DTO_생성() {
        MissionUpdateDto dto = new MissionUpdateDto();
        setField(dto, "boothId", 2L);
        return dto;
    }
}
