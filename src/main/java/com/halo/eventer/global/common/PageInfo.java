package com.halo.eventer.global.common;

import com.halo.eventer.domain.inquiry.Inquiry;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class PageInfo {
  private int pageNumber;
  private int pageSize;
  private long totalElements;
  private int totalPages;

  @Builder
  public PageInfo(int pageNumber, int pageSize, long totalElements, int totalPages) {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
  }
}
