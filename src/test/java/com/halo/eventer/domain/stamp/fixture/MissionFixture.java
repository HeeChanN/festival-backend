package com.halo.eventer.domain.stamp.fixture;

import java.util.List;

import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.dto.mission.request.MissionSetReqDto;
import com.halo.eventer.domain.stamp.dto.mission.response.MissionSummaryResDto;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class MissionFixture {

    public static final Long 미션1_ID = 1L;
    public static final Long 미션1_부스_ID = 1L;
    public static final String 미션1_제목 = "미션 1";
    public static final String 미션1_내용 = "미션1입니다.";
    public static final String 미션1_장소 = "미션1 장소";
    public static final String 미션1_시간 = "미션1 시간";
    public static final String 미션1_성공사진 = "미션1 성공 사진";
    public static final String 미션1_실패사진 = "미션1 실패 사진";

    public static final Long 미션2_ID = 2L;
    public static final Long 미션2_부스_ID = 2L;
    public static final String 미션2_제목 = "미션 2";
    public static final String 미션2_내용 = "미션2입니다.";
    public static final String 미션2_장소 = "미션2 장소";
    public static final String 미션2_시간 = "미션2 시간";
    public static final String 미션2_성공사진 = "미션2 성공 사진";
    public static final String 미션2_실패사진 = "미션2 실패 사진";

    public static Mission 미션1_생성() {
        return Mission.builder()
                .boothId(1L)
                .title("미션 1")
                .content("미션1입니다.")
                .place("미션1 장소")
                .time("미션1 시간")
                .clearedThumbnail("미션1 성공 사진")
                .notClearedThumbnail("미션1 실패 사진")
                .build();
    }

    public static Mission 미션2_생성() {
        return Mission.builder()
                .boothId(2L)
                .title("미션 2")
                .content("미션2입니다.")
                .place("미션2 장소")
                .time("미션2 시간")
                .clearedThumbnail("미션2 성공 사진")
                .notClearedThumbnail("미션2 실패 사진")
                .build();
    }

    public static List<MissionSetReqDto> 미션_셋업_리스트() {
        return List.of(
                new MissionSetReqDto(1L, "미션 1", "내용 1", "장소 1", "시간 1", "c1.jpg", "nc1.jpg"),
                new MissionSetReqDto(2L, "미션 2", "내용 2", "장소 2", "시간 2", "c2.jpg", "nc2.jpg"),
                new MissionSetReqDto(3L, "미션 3", "내용 3", "장소 3", "시간 3", "c3.jpg", "nc3.jpg"),
                new MissionSetReqDto(4L, "미션 4", "내용 4", "장소 4", "시간 4", "c4.jpg", "nc4.jpg"),
                new MissionSetReqDto(5L, "미션 5", "내용 5", "장소 5", "시간 5", "c5.jpg", "nc5.jpg"));
    }

    public static Mission 미션_엔티티_생성() {
        return Mission.from(미션_생성_DTO_생성());
    }

    public static MissionSetReqDto 미션_생성_DTO_생성() {
        MissionSetReqDto dto = new MissionSetReqDto();
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
        setField(dto, "title", "수정된 제목");
        setField(dto, "content", "수정된 내용");
        setField(dto, "place", "수정된 장소");
        setField(dto, "time", "14:00");
        setField(dto, "clearedThumbnail", "cleared.png");
        setField(dto, "notClearedThumbnail", "not-cleared.png");
        return dto;
    }

    public static MissionUpdateDto 미션_업데이트_DTO_일부수정_생성() {
        MissionUpdateDto dto = new MissionUpdateDto();
        setField(dto, "title", "수정된 제목");
        setField(dto, "content", "수정된 내용");
        return dto;
    }

    public static List<MissionSummaryResDto> 미션_조회_리스트_생성() {
        return List.of(
                new MissionSummaryResDto(미션1_ID, 미션1_제목, 미션1_성공사진, 미션1_실패사진),
                new MissionSummaryResDto(미션2_ID, 미션2_제목, 미션2_성공사진, 미션2_실패사진));
    }
}
