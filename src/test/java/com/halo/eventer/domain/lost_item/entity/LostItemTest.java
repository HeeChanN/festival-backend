package com.halo.eventer.domain.lost_item.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.lost_item.LostItem;
import com.halo.eventer.domain.lost_item.dto.LostItemReqDto;

import static com.halo.eventer.domain.festival.FestivalFixture.*;
import static com.halo.eventer.domain.lost_item.LostItemFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class LostItemTest {

    private LostItemReqDto lostItemReqDto;
    private Festival festival;

    @BeforeEach
    void setUp() {
        lostItemReqDto = LostItem_기본_생성_DTO();
        festival = 축제_엔티티();
    }

    @Test
    void LostItem_생성_테스트() {
        // when
        LostItem result = LostItem.of(lostItemReqDto, festival);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(lostItemReqDto.getName());
        assertThat(result.getDescription()).isEqualTo(lostItemReqDto.getDescription());
        assertThat(result.getThumbnail()).isEqualTo(lostItemReqDto.getThumbnail());
        assertThat(result.getFindDate()).isEqualTo(lostItemReqDto.getFindDate());
        assertThat(result.getFestival()).isEqualTo(festival);
    }

    @Test
    void LostItem_수정_테스트() {
        // given
        LostItem lostItem = LostItem_기본_엔티티();
        LostItemReqDto dto = LostItem_수정용_DTO();

        // when
        lostItem.update(dto);

        // then
        assertThat(lostItem.getName()).isEqualTo(dto.getName());
        assertThat(lostItem.getDescription()).isEqualTo(dto.getDescription());
        assertThat(lostItem.getThumbnail()).isEqualTo(dto.getThumbnail());
        assertThat(lostItem.getFindDate()).isEqualTo(dto.getFindDate());
    }
}
