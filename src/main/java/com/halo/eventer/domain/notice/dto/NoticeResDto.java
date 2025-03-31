package com.halo.eventer.domain.notice.dto;

import com.halo.eventer.domain.image.dto.ImageDto;
import com.halo.eventer.domain.notice.Notice;
import com.halo.eventer.domain.notice.ArticleType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeResDto {
    private Long id;
    private String title;
    private String thumbnail;
    private String tag;
    private String writer;
    private String content;
    private ArticleType type;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private boolean pick;
    private Long displayOrder;

    private List<ImageDto> images;

  @Builder
  public NoticeResDto(Long id,String title, String thumbnail, String tag, String writer, String content,
                      ArticleType type, LocalDateTime createAt, LocalDateTime updateAt, List<ImageDto> images,
                      boolean pick, Long displayOrder) {
    this.id = id;
    this.title = title;
    this.thumbnail = thumbnail;
    this.tag = tag;
    this.writer = writer;
    this.content = content;
    this.type = type;
    this.createAt = createAt;
    this.updateAt = updateAt;
    this.images = images;
    this.pick = pick;
    this.displayOrder = displayOrder;
  }

  public static NoticeResDto from(Notice notice) {
    return NoticeResDto.builder()
        .id(notice.getId())
        .title(notice.getTitle())
        .thumbnail(notice.getThumbnail())
        .tag(notice.getTag())
        .writer(notice.getWriter())
        .content(notice.getContent())
        .type(notice.getType())
        .createAt(notice.getCreatedAt())
        .updateAt(notice.getUpdatedAt())
        .images(notice.getImages().stream().map(ImageDto::from).collect(Collectors.toList()))
        .pick(notice.isPicked())
        .displayOrder(notice.getId())
        .build();
  }
}
