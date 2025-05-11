package com.halo.eventer.domain.festival;

import com.halo.eventer.domain.festival.dto.FestivalCreateDto;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class FestivalFixture {

    public static Festival 축제_엔티티() {
        return Festival.from(축제_생성용_DTO());
    }

    public static FestivalCreateDto 축제_생성용_DTO() {
        FestivalCreateDto festivalCreateDto = new FestivalCreateDto();
        setField(festivalCreateDto, "name", "축제");
        setField(festivalCreateDto, "subAddress", "주소");
        return festivalCreateDto;
    }
}
