package com.halo.eventer.domain.lost_item;

import java.time.LocalDate;
import javax.persistence.*;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.lost_item.dto.LostItemReqDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class LostItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "thumbnail", nullable = false, length = 1000)
    private String thumbnail;

    @Column(name = "find_date", nullable = false)
    private LocalDate findDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    private LostItem(LostItemReqDto lostItemReqDto, Festival festival) {
        this.name = lostItemReqDto.getName();
        this.thumbnail = lostItemReqDto.getThumbnail();
        this.findDate = lostItemReqDto.getFindDate();
        this.description = lostItemReqDto.getDescription();
        this.festival = festival;
    }

    public static LostItem of(LostItemReqDto lostDto, Festival festival) {
        return new LostItem(lostDto, festival);
    }

    public void update(LostItemReqDto lostItemReqDto) {
        this.name = lostItemReqDto.getName();
        this.thumbnail = lostItemReqDto.getThumbnail();
        this.findDate = lostItemReqDto.getFindDate();
        this.description = lostItemReqDto.getDescription();
    }
}
