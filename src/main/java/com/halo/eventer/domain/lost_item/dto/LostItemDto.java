package com.halo.eventer.domain.lost_item.dto;

import com.halo.eventer.domain.lost_item.LostItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LostItemDto {
    private String name;
    private String type;
    private String thumbnail;
    private String findDate;

    public LostItemDto(LostItem lostItem) {
        this.name = lostItem.getName();
        this.type = lostItem.getType();
        this.thumbnail = lostItem.getThumbnail();
        this.findDate = lostItem.getFindDate();
    }
}
