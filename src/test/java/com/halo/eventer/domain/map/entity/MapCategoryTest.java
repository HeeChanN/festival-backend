package com.halo.eventer.domain.map.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.map.MapCategory;
import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryImageDto;
import com.halo.eventer.domain.map.enumtype.MapCategoryType;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class MapCategoryTest {

    private Festival festival;
    private MapCategory mapCategory;

    @BeforeEach
    void setUp() {
        festival = new Festival();
        mapCategory = MapCategory.of(festival, "카테고리");
    }

    @Test
    void 지도_카테고리_생성() {
        // when
        MapCategory target = MapCategory.of(festival, "카테고리");

        // then
        assertThat(target).isNotNull();
        assertThat(target.getCategoryName()).isEqualTo("카테고리");
        assertThat(target.getFestival()).isEqualTo(festival);
    }

    @Test
    void 고정_카테고리로_생성() {
        // when
        MapCategory target = MapCategory.createFixedBooth();

        // then
        assertThat(target).isNotNull();
        assertThat(target.getCategoryName()).isEqualTo(MapCategoryType.FIXED_BOOTH.getDisplayName());
    }

    @Test
    void 아이콘_고정핀_수정() {
        // given
        MapCategoryImageDto mapCategoryImageDto = MapCategoryImageDto.of("핀", "아이콘");

        // when
        mapCategory.updateIconAndPin(mapCategoryImageDto);

        // then
        assertThat(mapCategory.getIcon()).isEqualTo("아이콘");
        assertThat(mapCategory.getPin()).isEqualTo("핀");
    }

    @Test
    void 순서_수정() {
        // when
        mapCategory.updateDisplayOrder(1);

        // then
        assertThat(mapCategory.getDisplayOrder()).isEqualTo(1);
    }
}
