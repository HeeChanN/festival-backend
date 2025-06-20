package com.halo.eventer.domain.lost_item.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.lost_item.LostItem;
import com.halo.eventer.domain.lost_item.dto.LostItemReqDto;
import com.halo.eventer.domain.lost_item.dto.LostItemResDto;
import com.halo.eventer.domain.lost_item.dto.LostItemSummaryDto;
import com.halo.eventer.domain.lost_item.exception.LostItemNotFoundException;
import com.halo.eventer.domain.lost_item.repository.LostItemRepository;

import static com.halo.eventer.domain.festival.FestivalFixture.축제_엔티티;
import static com.halo.eventer.domain.lost_item.LostItemFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class LostItemServiceTest {
    @Mock
    private LostItemRepository lostItemRepository;

    @Mock
    private FestivalRepository festivalRepository;

    @InjectMocks
    private LostItemService lostItemService;

    final Long festivalId = 1L;
    private Festival festival;
    private LostItem lostItem;
    private LostItemReqDto lostItemReqDto;

    @BeforeEach
    void setUp() {
        festival = 축제_엔티티();
        lostItem = LostItem_기본_엔티티();
        lostItemReqDto = LostItem_기본_생성_DTO();
    }

    @Test
    void LostItem_생성_성공_테스트() {
        // given
        given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
        given(lostItemRepository.save(any())).willReturn(lostItem);

        // when
        Long result = lostItemService.create(festivalId, lostItemReqDto);

        // then
        assertThat(result).isEqualTo(lostItem.getId());
        verify(festivalRepository, times(1)).findById(festivalId);
        verify(lostItemRepository, times(1)).save(any());
    }

    @Test
    void LostItem_생성_festival_없을_경우_예외() {
        // given
        given(festivalRepository.findById(festivalId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> lostItemService.create(festivalId, lostItemReqDto))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void LostItem_수정_성공_테스트() {
        // given
        given(lostItemRepository.findById(lostItem.getId())).willReturn(Optional.of(lostItem));
        LostItemReqDto dto = LostItem_수정용_DTO();

        // when
        LostItemResDto result = lostItemService.update(lostItem.getId(), dto);

        // then
        assertThat(result.getId()).isEqualTo(lostItem.getId());
        assertThat(result.getName()).isEqualTo(dto.getName());
        assertThat(result.getDescription()).isEqualTo(dto.getDescription());
        assertThat(result.getThumbnail()).isEqualTo(dto.getThumbnail());
        assertThat(result.getFindDate()).isEqualTo(dto.getFindDate());
    }

    @Test
    void LostItem_수정_LostItem_없는_경우_예외() {
        // given
        given(lostItemRepository.findById(lostItem.getId())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> lostItemService.update(lostItem.getId(), lostItemReqDto))
                .isInstanceOf(LostItemNotFoundException.class);
    }

    @Test
    void LostItem_삭제_테스트() {
        // given
        given(lostItemRepository.findById(lostItem.getId())).willReturn(Optional.of(lostItem));
        doNothing().when(lostItemRepository).delete(any(LostItem.class));

        // when
        Long result = lostItemService.delete(lostItem.getId());

        // then
        assertThat(result).isEqualTo(lostItem.getFestival().getId());
    }

    @Test
    void LostItem_단일조회_테스트() {
        // given
        given(lostItemRepository.findById(lostItem.getId())).willReturn(Optional.of(lostItem));

        // when
        LostItemResDto result = lostItemService.getLostItem(lostItem.getId());

        // then
        assertThat(result.getId()).isEqualTo(lostItem.getId());
        assertThat(result.getName()).isEqualTo(lostItem.getName());
        assertThat(result.getDescription()).isEqualTo(lostItem.getDescription());
        assertThat(result.getThumbnail()).isEqualTo(lostItem.getThumbnail());
        assertThat(result.getFindDate()).isEqualTo(lostItem.getFindDate());
    }

    @Test
    void LostItem_전체조회_테스트() {
        // given
        given(lostItemRepository.findAllByFestivalId(festivalId)).willReturn(List.of(lostItem));

        // when
        List<LostItemSummaryDto> results = lostItemService.getLostItemsByFestivalId(festivalId);

        // given
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getId()).isEqualTo(lostItem.getId());
        assertThat(results.get(0).getName()).isEqualTo(lostItem.getName());
        assertThat(results.get(0).getDescription()).isEqualTo(lostItem.getDescription());
    }
}
