package com.halo.eventer.domain.lost_item.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.lost_item.LostItem;
import com.halo.eventer.domain.lost_item.dto.LostItemDto;
import com.halo.eventer.domain.lost_item.exception.LostItemNotFoundException;
import com.halo.eventer.domain.lost_item.repository.LostItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LostItemService {
    private final LostItemRepository lostItemRepository;
    private final FestivalRepository festivalRepository;

    // 분실물 등록
    @Transactional
    public LostItem createLostItem(Long festivalId, LostItemDto lostDto) {
        Festival festival =
                festivalRepository.findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));
        return lostItemRepository.save(new LostItem(lostDto, festival));
    }

    // 분실물 수정
    @Transactional
    public LostItem updateLostItem(Long id, LostItemDto lostDto) {
        LostItem item = lostItemRepository.findById(id).orElseThrow(() -> new LostItemNotFoundException(id));
        item.updateItem(lostDto);
        return item;
    }

    // 분실물 삭제
    @Transactional
    public void deleteLostItem(Long id) {
        lostItemRepository.delete(lostItemRepository.findById(id).orElseThrow(() -> new LostItemNotFoundException(id)));
    }

    // 분실물 단일 조회
    public LostItem getLostItem(Long id) {
        return lostItemRepository.findById(id).orElseThrow(() -> new LostItemNotFoundException(id));
    }

    // 분실물 전체 조회
    public List<LostItem> getAllLostItems() {
        return lostItemRepository.findAll();
    }
}
