package com.halo.eventer.domain.lost_item.controller;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.lost_item.dto.LostItemReqDto;
import com.halo.eventer.domain.lost_item.dto.LostItemResDto;
import com.halo.eventer.domain.lost_item.dto.LostItemSummaryDto;
import com.halo.eventer.domain.lost_item.service.LostItemService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LostItemController {

    private final LostItemService lostItemService;

    @PostMapping("/lost-items")
    public void create(
            @Min(1) @RequestParam("festivalId") Long festivalId, @Valid @RequestBody LostItemReqDto lostItemReqDto) {
        lostItemService.create(festivalId, lostItemReqDto);
    }

    @PutMapping("/lost-items")
    public LostItemResDto update(
            @Min(1) @RequestParam(name = "id") Long id, @Valid @RequestBody LostItemReqDto lostItemReqDto) {
        return lostItemService.update(id, lostItemReqDto);
    }

    @DeleteMapping("/lost-items")
    public void delete(@Min(1) @RequestParam(name = "id") Long id) {
        lostItemService.delete(id);
    }

    @GetMapping("/lost-items/{id}")
    public LostItemResDto getLostItem(@Min(1) @PathVariable(name = "id") Long id) {
        return lostItemService.getLostItem(id);
    }

    @GetMapping("/{festivalId}/lost-items")
    public List<LostItemSummaryDto> getLostItems(@Min(1) @PathVariable("festivalId") Long festivalId) {
        return lostItemService.getLostItemsByFestivalId(festivalId);
    }
}
