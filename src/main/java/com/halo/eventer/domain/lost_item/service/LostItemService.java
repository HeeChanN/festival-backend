package com.halo.eventer.domain.lost_item.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.lost_item.LostItem;
import com.halo.eventer.domain.lost_item.dto.LostItemReqDto;
import com.halo.eventer.domain.lost_item.dto.LostItemResDto;
import com.halo.eventer.domain.lost_item.dto.LostItemSummaryDto;
import com.halo.eventer.domain.lost_item.exception.LostItemNotFoundException;
import com.halo.eventer.domain.lost_item.repository.LostItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LostItemService {
    private final LostItemRepository lostItemRepository;
    private final FestivalRepository festivalRepository;

    @Transactional
    public Long create(Long festivalId, LostItemReqDto lostItemReqDto) {
        Festival festival =
                festivalRepository.findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));
        LostItem lostItem = lostItemRepository.save(LostItem.of(lostItemReqDto, festival));
        return lostItem.getId();
    }

    @Transactional
    public LostItemResDto update(Long id, LostItemReqDto lostDto) {
        LostItem lostItem = loadFestivalOrThrow(id);
        lostItem.update(lostDto);
        return LostItemResDto.from(lostItem);
    }

    @Transactional
    public Long delete(Long id) {
        LostItem lostItem = loadFestivalOrThrow(id);
        Long festivalId = lostItem.getFestival().getId();
        lostItemRepository.delete(lostItem);
        return festivalId;
    }

    public LostItemResDto getLostItem(Long id) {
        LostItem lostItem = loadFestivalOrThrow(id);
        return LostItemResDto.from(lostItem);
    }

    // TODO: 페이징 조회로 변경 (Client 용, 백오피스 용)
    public List<LostItemSummaryDto> getLostItemsByFestivalId(Long festivalId) {
        return lostItemRepository.findAllByFestivalId(festivalId).stream()
                .map(LostItemSummaryDto::new)
                .collect(Collectors.toList());
    }

    private LostItem loadFestivalOrThrow(Long id) {
        return lostItemRepository.findById(id).orElseThrow(() -> new LostItemNotFoundException(id));
    }
}
