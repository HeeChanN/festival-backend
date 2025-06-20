package com.halo.eventer.domain.lost_item.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.lost_item.dto.LostItemReqDto;
import com.halo.eventer.domain.lost_item.dto.LostItemResDto;
import com.halo.eventer.domain.lost_item.dto.LostItemSummaryDto;
import com.halo.eventer.domain.lost_item.service.LostItemService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lostItems")
@RequiredArgsConstructor
public class LostItemController {

    private final LostItemService lostItemService;

    @PostMapping()
    public List<LostItemSummaryDto> createAndGetLostItems(
            @RequestParam("festivalId") Long festivalId, @RequestBody LostItemReqDto lostItemReqDto) {
        lostItemService.create(festivalId, lostItemReqDto);
        return lostItemService.getLostItemsByFestivalId(festivalId);
    }

    @PatchMapping("/{id}")
    public LostItemResDto update(@PathVariable(name = "id") Long id, @RequestBody LostItemReqDto lostItemReqDto) {
        return lostItemService.update(id, lostItemReqDto);
    }

    @DeleteMapping("/{id}")
    public List<LostItemSummaryDto> deleteAndGetLostItems(@PathVariable(name = "id") Long id) {
        Long festivalId = lostItemService.delete(id);
        return lostItemService.getLostItemsByFestivalId(festivalId);
    }

    @GetMapping("/{id}")
    public LostItemResDto getLostItem(@PathVariable(name = "id") Long id) {
        return lostItemService.getLostItem(id);
    }

    @GetMapping()
    public List<LostItemSummaryDto> getLostItems(@RequestParam("festivalId") Long festivalId) {
        return lostItemService.getLostItemsByFestivalId(festivalId);
    }
}
