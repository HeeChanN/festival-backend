package com.halo.eventer.domain.stamp.fixture;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.dto.FestivalCreateDto;
import com.halo.eventer.domain.stamp.Stamp;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class StampFixture {

    public static Stamp 스탬프_엔티티_생성() {
        Stamp stamp = Stamp.create(Festival.from(new FestivalCreateDto("test festival", "test address")));
        setField(stamp, "id", 1L);
        return stamp;
    }
}
