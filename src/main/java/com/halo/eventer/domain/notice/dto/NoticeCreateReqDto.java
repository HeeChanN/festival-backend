package com.halo.eventer.domain.notice.dto;

import java.util.List;

import com.halo.eventer.domain.notice.ArticleType;
import lombok.*;

@NoArgsConstructor
@Getter
public class NoticeCreateReqDto {
    private String title;
    private String content;
    private String tag;
    private String writer;
    private String thumbnail;
    private ArticleType type;
    private List<String> images;
}
