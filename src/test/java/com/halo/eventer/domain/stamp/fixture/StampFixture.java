package com.halo.eventer.domain.stamp.fixture;

import java.util.List;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.dto.stamp.StampGetDto;

@SuppressWarnings("NonAsciiCharacters")
public class StampFixture {

    public static Long 존재하지_않는_스탬프 = 999L;

    public static final Long 스탬프1_ID = 1L;
    public static final boolean 스탬프1_활성화 = true;
    public static final int 스탬프1_완료_카운트 = 4;

    public static final Long 스탬프2_ID = 2L;
    public static final boolean 스탬프2_활성화 = true;
    public static final int 스탬프2_완료_카운트 = 7;

    public static final Long 스탬프3_ID = 3L;
    public static final boolean 스탬프3_활성화 = true;
    public static final int 스탬프3_완료_카운트 = 10;

    public static Stamp 스탬프1_생성(Festival festival) {
        return Stamp.create(festival);
    }

    public static Stamp 스탬프2_생성(Festival festival) {
        return Stamp.create(festival);
    }

    public static Stamp 스탬프3_생성(Festival festival) {
        return Stamp.create(festival);
    }

    public static List<Stamp> 모든_스탬프(Festival festival) {
        return List.of(스탬프1_생성(festival), 스탬프2_생성(festival), 스탬프3_생성(festival));
    }

    public static List<StampGetDto> 스탬프_목록_DTO_생성() {
        return List.of(
                new StampGetDto(스탬프1_ID, 스탬프1_활성화, 스탬프1_완료_카운트),
                new StampGetDto(스탬프2_ID, 스탬프2_활성화, 스탬프2_완료_카운트),
                new StampGetDto(스탬프3_ID, 스탬프3_활성화, 스탬프3_완료_카운트));
    }
}
