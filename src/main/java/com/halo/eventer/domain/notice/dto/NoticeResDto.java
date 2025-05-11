package com.halo.eventer.domain.notice.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.domain.image.Image;
import com.halo.eventer.domain.image.dto.ImageDto;
import com.halo.eventer.domain.notice.ArticleType;
import com.halo.eventer.domain.notice.Notice;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean pick;
    private Integer displayOrder;
    private List<ImageDto> images;

    @Builder
    private NoticeResDto(
            Long id,
            String title,
            String thumbnail,
            String tag,
            String writer,
            String content,
            ArticleType type,
            LocalDateTime createAt,
            LocalDateTime updateAt,
            List<ImageDto> images,
            boolean pick,
            Integer displayOrder) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.tag = tag;
        this.writer = writer;
        this.content = content;
        this.type = type;
        this.createdAt = createAt;
        this.updatedAt = updateAt;
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
                .images(toImageDtos(notice.getImages()))
                .pick(notice.isPicked())
                .displayOrder(notice.getDisplayOrder())
                .build();
    }

    private static List<ImageDto> toImageDtos(List<Image> images) {
        return images.stream().map(ImageDto::from).collect(Collectors.toList());
    }
}
