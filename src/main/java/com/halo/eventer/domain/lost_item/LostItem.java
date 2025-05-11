package com.halo.eventer.domain.lost_item;

import javax.persistence.*;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.lost_item.dto.LostItemDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class LostItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String thumbnail;
    private String findDate;
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    public LostItem(LostItemDto lostDto, Festival festival) {
        this.name = lostDto.getName();
        this.thumbnail = lostDto.getThumbnail();
        this.findDate = lostDto.getFindDate();
        this.type = lostDto.getType();
        this.festival = festival;
    }

    public void updateItem(LostItemDto lostDto) {
        this.name = lostDto.getName();
        this.thumbnail = lostDto.getThumbnail();
        this.findDate = lostDto.getFindDate();
        this.type = lostDto.getType();
    }
}
