package com.halo.eventer.domain.lost_item.controller;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.lost_item.dto.LostItemDto;
import com.halo.eventer.domain.lost_item.dto.LostItemLIstDto;
import com.halo.eventer.domain.lost_item.service.LostItemService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lostItem")
@RequiredArgsConstructor
public class LostItemController {

    private final LostItemService lostItemService;

    // 분실물 등록
    @PostMapping()
    public LostItemLIstDto createLostItem(
            @RequestParam("festivalId") Long festivalId, @RequestBody LostItemDto lostItemDto) {
        lostItemService.createLostItem(festivalId, lostItemDto);
        return new LostItemLIstDto(lostItemService.getAllLostItems());
    }

    // 분실물 수정
    @PatchMapping("/{lostItemId}")
    public LostItemLIstDto updateLostItem(
            @PathVariable(name = "lostItemId") Long lostItemId, @RequestBody LostItemDto lostDto) {
        lostItemService.updateLostItem(lostItemId, lostDto);
        return new LostItemLIstDto(lostItemService.getAllLostItems());
    }

    // 분실물 삭제
    @DeleteMapping("/{itemId}")
    public LostItemLIstDto deleteLostItem(@PathVariable(name = "itemId") Long itemId) {
        lostItemService.deleteLostItem(itemId);
        return new LostItemLIstDto(lostItemService.getAllLostItems());
    }

    // 분실물 단일 조회
    @GetMapping("/{lostItemId}")
    public LostItemDto getLostItem(@PathVariable(name = "lostItemId") Long itemId) {
        return new LostItemDto(lostItemService.getLostItem(itemId));
    }

    // 분실물 전체 조회
    @GetMapping()
    public LostItemLIstDto getAllLostItems() {
        return new LostItemLIstDto(lostItemService.getAllLostItems());
    }
}
