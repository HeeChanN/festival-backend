package com.halo.eventer.domain.lost_item;

import java.time.LocalDate;

import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.lost_item.dto.LostItemReqDto;
import com.halo.eventer.domain.lost_item.dto.LostItemResDto;
import com.halo.eventer.domain.lost_item.dto.LostItemSummaryDto;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class LostItemFixture {

    public static final Long 분실물_1번_id = 1L;
    public static final Long 분실물_2번_id = 2L;

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

    public static LostItemResDto 분실물_1번_응답_DTO() {
        return LostItemResDto.of(분실물_1번_id, 기본_이름, 기본_설명, 기본_썸네일, 기본_날짜);
    }

    public static LostItemSummaryDto 분실물_1번_요약_DTO() {
        return new LostItemSummaryDto(분실물_1번_id, 기본_이름, 기본_설명);
    }

    public static LostItemSummaryDto 분실물_2번_요약_DTO() {
        return new LostItemSummaryDto(분실물_2번_id, 기본_이름, 기본_설명);
    }
}
