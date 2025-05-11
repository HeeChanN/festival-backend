package com.halo.eventer.domain.map;

import com.halo.eventer.domain.festival.FestivalFixture;

@SuppressWarnings("NonAsciiCharacters")
public class MapCategoryFixture {

    public static MapCategory 지도카테고리_엔티티() {
        return MapCategory.of(FestivalFixture.축제_엔티티(), "카테고리");
    }
}
