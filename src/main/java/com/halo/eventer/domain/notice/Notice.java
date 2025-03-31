package com.halo.eventer.domain.notice;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.image.Image;
import com.halo.eventer.domain.notice.dto.NoticeCreateDto;
import com.halo.eventer.domain.notice.dto.NoticeUpdateDto;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.halo.eventer.global.common.BaseTime;
import com.halo.eventer.global.constants.DisplayOrderConstants;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
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

  @OneToMany(mappedBy = "notice", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  private List<Image> images = new ArrayList<>();

  private Integer displayOrder;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "festival_id")
  private Festival festival;

  @Builder
  public Notice(String tag, String title, String writer, String content, String thumbnail,
                ArticleType type, Festival festival) {
    this.tag = tag;
    this.title = title;
    this.writer = writer;
    this.content = content;
    this.thumbnail = thumbnail;
    this.type = type;
    this.festival = festival;
    this.picked = false;
    this.displayOrder = DisplayOrderConstants.DISPLAY_ORDER_DEFAULT;
  }

  public static Notice from(Festival festival, NoticeCreateDto dto) {
    return Notice.builder()
            .tag(dto.getTag())
            .title(dto.getTitle())
            .writer(dto.getWriter())
            .content(dto.getContent())
            .thumbnail(dto.getThumbnail())
            .type(dto.getType())
            .festival(festival)
            .build();
  }

  public void updatePicked(boolean picked) {
    this.picked = picked;
    if (!picked) {
      this.displayOrder = DisplayOrderConstants.DISPLAY_ORDER_DEFAULT;
    }
  }

  public void updateNotice(NoticeUpdateDto n) {
    this.title = n.getTitle();
    this.content = n.getContent();
    this.thumbnail = n.getThumbnail();
    this.tag = n.getTag();
    this.writer = n.getWriter();
    this.type = n.getType();
  }

  public void setRank(Integer rank) {
    this.displayOrder = rank;
  }

  public void addImages(List<String> newImages) {
    newImages.forEach(o -> this.images.add(new Image(o)));
    images.forEach(o -> o.setNotice(this));
  }
}
