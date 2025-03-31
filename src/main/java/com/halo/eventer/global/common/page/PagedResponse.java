package com.halo.eventer.global.common.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedResponse<T> {
  private List<T> content;
  private PageInfo pageInfo;
}
