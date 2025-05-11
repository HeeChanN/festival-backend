package com.halo.eventer.domain.notice.dto;

import java.util.List;

import com.halo.eventer.domain.notice.ArticleType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeUpdateReqDto {
    private String title;
    private String content;
    private String tag;
    private String writer;
    private String thumbnail;
    private ArticleType type;
    private List<String> images;
    private List<Long> deleteIds;
}
