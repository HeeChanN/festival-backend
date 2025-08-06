package com.halo.eventer.domain.lost_item.dto;

import com.halo.eventer.domain.lost_item.LostItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LostItemSummaryDto {
    private Long id;
    private String name;
    private String description;

    public LostItemSummaryDto(LostItem lostItem) {
        this.id = lostItem.getId();
        this.name = lostItem.getName();
        this.description = lostItem.getDescription();
    }

    public LostItemSummaryDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
