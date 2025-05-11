package com.halo.eventer.domain.festival.entity;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.dto.*;
import com.halo.eventer.domain.image.dto.FileDto;
import com.halo.eventer.domain.map.MapCategory;
import com.halo.eventer.domain.map.enumtype.MapCategoryType;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class FestivalTest {

    private FestivalCreateDto festivalCreateDto;
    private Festival festival;

    @BeforeEach
    void setUp() {
        festivalCreateDto = new FestivalCreateDto("축제", "univ");
        festival = Festival.from(festivalCreateDto);
    }

    @Test
    void 축제객체_생성() {
        // when
        Festival target = Festival.from(festivalCreateDto);

        // then
        assertThat(target.getName()).isEqualTo(festival.getName());
        assertThat(target.getSubAddress()).isEqualTo(festivalCreateDto.getSubAddress());
    }

    @Test
    void 고정부스_추가() {
        // when
        festival.applyDefaultMapCategory();
        List<MapCategory> mapCategories = festival.getMapCategories();
        Optional<MapCategory> mapCategory = mapCategories.stream().findFirst();

        // then
        assertThat(mapCategories).hasSize(1);
        assertThat(mapCategory.get().getCategoryName()).isEqualTo(MapCategoryType.FIXED_BOOTH.getDisplayName());
    }

    @Test
    void 축제기본정보_수정() {
        // given
        FestivalCreateDto festivalCreateDto = new FestivalCreateDto("수정", "edit");

        // when
        festival.updateFestival(festivalCreateDto);

        // then
        assertThat(festival.getName()).isEqualTo(festivalCreateDto.getName());
        assertThat(festival.getSubAddress()).isEqualTo(festivalCreateDto.getSubAddress());
    }

    @Test
    void 색_수정() {
        // given
        ColorDto colorDto = new ColorDto("#FFFFFF", "#000000", "#CCCCCC", "#F0F0F0");

        // when
        festival.updateColor(colorDto);

        // then
        assertThat(festival.getMainColor()).isEqualTo(colorDto.getMainColor());
        assertThat(festival.getSubColor()).isEqualTo(colorDto.getSubColor());
        assertThat(festival.getFontColor()).isEqualTo(colorDto.getFontColor());
        assertThat(festival.getBackgroundColor()).isEqualTo(colorDto.getBackgroundColor());
    }

    @Test
    void 로고_수정() {
        // given
        FileDto imageDto = new FileDto("logo.png");

        // when
        festival.updateLogo(imageDto);

        // then
        assertThat(festival.getLogo()).isEqualTo(imageDto.getUrl());
    }

    @Test
    void 위치정보_수정() {
        // given
        FestivalLocationDto locationDto = new FestivalLocationDto(127.1111, 37.2222);

        festival.updateLocation(locationDto);

        assertThat(festival.getLongitude()).isEqualTo(locationDto.getLongitude());
        assertThat(festival.getLatitude()).isEqualTo(locationDto.getLatitude());
    }
}
