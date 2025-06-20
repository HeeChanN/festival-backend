package com.halo.eventer.domain.lost_item;

import java.time.LocalDate;

import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.lost_item.dto.LostItemReqDto;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class LostItemFixture {

    static final String 기본_설명 = "description";
    static final String 기본_이름 = "name";
    static final String 기본_썸네일 = "thumbnail";
    static final LocalDate 기본_날짜 = LocalDate.of(2025, 1, 1);

    public static LostItem LostItem_기본_엔티티() {
        LostItem lostItem = LostItem.of(LostItem_기본_생성_DTO(), FestivalFixture.축제_엔티티());
        setField(lostItem, "id", 1L);
        return lostItem;
    }

    public static LostItemReqDto LostItem_기본_생성_DTO() {
        return LostItemReqDto.builder()
                .description(기본_설명)
                .name(기본_이름)
                .thumbnail(기본_썸네일)
                .findDate(기본_날짜)
                .build();
    }

    public static LostItemReqDto LostItem_수정용_DTO() {
        return LostItemReqDto.builder()
                .description("updated_description")
                .name("updated_name")
                .thumbnail("updated_thumbnail")
                .findDate(LocalDate.of(2025, 1, 2))
                .build();
    }
}
