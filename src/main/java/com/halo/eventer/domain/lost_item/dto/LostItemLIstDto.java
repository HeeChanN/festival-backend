package com.halo.eventer.domain.lost_item.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.domain.lost_item.LostItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LostItemLIstDto {
    private List<LostItemElementDto> lostItems;

    public LostItemLIstDto(List<LostItem> lostItems) {
        this.lostItems = lostItems.stream().map(LostItemElementDto::new).collect(Collectors.toList());
    }
}
