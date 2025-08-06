package com.halo.eventer.domain.stamp.fixture;

import java.util.List;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.stamp.Stamp;

import static com.halo.eventer.domain.festival.FestivalFixture.축제_엔티티;

@SuppressWarnings("NonAsciiCharacters")
public class StampFixture {

    public static Long 존재하지_않는_스탬프 = 999L;

    public static Long 스탬프1 = 1L;
    public static Long 스탬프2 = 2L;
    public static Long 스탬프3 = 3L;

    public static Festival festival = 축제_엔티티();

    public static Stamp 스탬프1_생성() {
        return Stamp.create(festival);
    }

    public static Stamp 스탬프2_생성() {
        return Stamp.create(festival);
    }

    public static Stamp 스탬프3_생성() {
        return Stamp.create(festival);
    }

    public static List<Stamp> 모든_스탬프() {
        return List.of(스탬프1_생성(), 스탬프2_생성(), 스탬프3_생성());
    }
}
