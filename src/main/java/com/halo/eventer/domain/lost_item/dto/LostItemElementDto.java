package com.halo.eventer.domain.lost_item.dto;

import com.halo.eventer.domain.lost_item.LostItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LostItemElementDto {
    private Long id;
    private String name;
    private String type;
    private String thumbnail;
    private String findDate;

    public LostItemElementDto(LostItem lostItem) {
        this.id = lostItem.getId();
        this.name = lostItem.getName();
        this.type = lostItem.getType();
        this.thumbnail = lostItem.getThumbnail();
        this.findDate = lostItem.getFindDate();
    }
}
