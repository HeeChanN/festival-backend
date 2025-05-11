package com.halo.eventer.domain.notice.dto.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.domain.image.Image;
import com.halo.eventer.domain.image.dto.ImageDto;
import com.halo.eventer.domain.notice.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserNoticeResDto {
    private String title;
    private String thumbnail;
    private String tag;
    private String writer;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<ImageDto> images;

    @Builder
    public UserNoticeResDto(
            String title,
            String thumbnail,
            String tag,
            String writer,
            String content,
            LocalDateTime createAt,
            LocalDateTime updateAt,
            List<ImageDto> images) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.tag = tag;
        this.writer = writer;
        this.content = content;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.images = images;
    }

    public static UserNoticeResDto from(Notice notice) {
        return UserNoticeResDto.builder()
                .title(notice.getTitle())
                .thumbnail(notice.getThumbnail())
                .tag(notice.getTag())
                .writer(notice.getWriter())
                .content(notice.getContent())
                .createAt(notice.getCreatedAt())
                .updateAt(notice.getUpdatedAt())
                .images(toImageDtos(notice.getImages()))
                .build();
    }

    private static List<ImageDto> toImageDtos(List<Image> images) {
        return images.stream().map(ImageDto::from).collect(Collectors.toList());
    }
}
