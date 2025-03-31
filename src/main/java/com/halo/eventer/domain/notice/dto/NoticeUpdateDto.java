package com.halo.eventer.domain.notice.dto;

import com.halo.eventer.domain.notice.ArticleType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NoticeUpdateDto {
  private List<Long> deleteIds;
  private String title;
  private String content;
  private String tag;
  private String writer;
  private String thumbnail;
  private ArticleType type;
  private List<String> images;
}
