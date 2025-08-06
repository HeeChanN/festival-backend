package com.halo.eventer.domain.notice.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.domain.notice.ArticleType;
import com.halo.eventer.domain.notice.Notice;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PickedNoticeResDto {
    private Long id;
    private Integer displayOrder;
    private String thumbnail;
    private ArticleType type;

    public PickedNoticeResDto(Notice n) {
        this.id = n.getId();
        this.thumbnail = n.getThumbnail();
        this.displayOrder = n.getDisplayOrder();
        this.type = n.getType();
    }

    public PickedNoticeResDto(Long id, Integer displayOrder, String thumbnail, ArticleType type) {
        this.id = id;
        this.displayOrder = displayOrder;
        this.thumbnail = thumbnail;
        this.type = type;
    }

    public static List<PickedNoticeResDto> noticesToPikedNoticeResDtos(List<Notice> noticeList) {
        return noticeList.stream().map(PickedNoticeResDto::new).collect(Collectors.toList());
    }
}
