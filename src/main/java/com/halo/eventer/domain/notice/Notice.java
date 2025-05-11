package com.halo.eventer.domain.notice;

import java.util.*;
import javax.persistence.*;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.image.Image;
import com.halo.eventer.domain.notice.dto.NoticeCreateReqDto;
import com.halo.eventer.domain.notice.dto.NoticeUpdateReqDto;
import com.halo.eventer.domain.notice.exception.MissingNoticeException;
import com.halo.eventer.global.common.BaseTime;
import com.halo.eventer.global.constants.DisplayOrderConstants;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "notice",
        indexes = {@Index(name = "idx_notice_paging", columnList = "festival_id, type, updated_at DESC")})
public class Notice extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;
    private String title;
    private String writer;

    @Column(columnDefinition = "varchar(3000)")
    private String content;

    private String thumbnail;

    private boolean picked;

    @Enumerated(EnumType.STRING)
    private ArticleType type;

    @OneToMany(
            mappedBy = "notice",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Image> images = new ArrayList<>();

    private Integer displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @Builder
    private Notice(
            String tag,
            String title,
            String writer,
            String content,
            String thumbnail,
            ArticleType type,
            Festival festival,
            List<String> images) {
        this.tag = tag;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.thumbnail = thumbnail;
        this.type = type;
        this.festival = festival;
        this.picked = false;
        this.displayOrder = DisplayOrderConstants.DISPLAY_ORDER_DEFAULT;
        addImages(images);
    }

    public static Notice from(Festival festival, NoticeCreateReqDto dto) {
        return Notice.builder()
                .tag(dto.getTag())
                .title(dto.getTitle())
                .writer(dto.getWriter())
                .content(dto.getContent())
                .thumbnail(dto.getThumbnail())
                .type(dto.getType())
                .festival(festival)
                .images(dto.getImages())
                .build();
    }

    public void updatePicked(boolean picked) {
        this.picked = picked;
        if (!picked) {
            this.displayOrder = DisplayOrderConstants.DISPLAY_ORDER_DEFAULT;
        }
    }

    public void updateNotice(NoticeUpdateReqDto noticeUpdateReqDto) {
        this.title = noticeUpdateReqDto.getTitle();
        this.content = noticeUpdateReqDto.getContent();
        this.thumbnail = noticeUpdateReqDto.getThumbnail();
        this.tag = noticeUpdateReqDto.getTag();
        this.writer = noticeUpdateReqDto.getWriter();
        this.type = noticeUpdateReqDto.getType();
        deleteImages(noticeUpdateReqDto.getDeleteIds());
        addImages(noticeUpdateReqDto.getImages());
    }

    public static void reOrderPickedNotices(List<Notice> notices, Map<Long, Integer> newOrders) {
        notices.forEach(n -> {
            Integer newDisplayOrder = newOrders.get(n.getId());
            if (newDisplayOrder == null) throw new MissingNoticeException(n.getId());
            n.changeDisplayOrder(newDisplayOrder);
        });
    }

    private void addImages(List<String> newImages) {
        newImages.stream().map(o -> Image.ofNotice(o, this)).forEach(o -> images.add(o));
    }

    private void deleteImages(List<Long> imageIds) {
        Set<Long> idsToDelete = new HashSet<>(imageIds);
        images.removeIf(image -> idsToDelete.contains(image.getId()));
    }

    private void changeDisplayOrder(Integer newDisplayOrder) {
        this.displayOrder = newDisplayOrder;
    }
}
