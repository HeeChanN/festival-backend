package com.halo.eventer.domain.widget.controller;

import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetResDto;
import com.halo.eventer.domain.widget.service.UpWidgetService;
import com.halo.eventer.global.common.sort.SortOption;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/upWidgets")
@Validated
@Tag(name = "상단 위젯 API")
public class UpWidgetController {

  private final UpWidgetService upWidgetService;

  @PostMapping()
  public UpWidgetResDto create(@RequestParam(name = "festivalId") Long festivalId,
                                       @RequestBody UpWidgetCreateDto upWidgetCreateDto) {
    return upWidgetService.create(festivalId, upWidgetCreateDto);
  }

  @GetMapping("/{upWidgetId}")
  public UpWidgetResDto getUpWidget(@PathVariable("upWidgetId") Long upWidgetId) {
    return upWidgetService.getUpWidget(upWidgetId);
  }

  @GetMapping
  public PagedResponse<UpWidgetResDto> getUpWidgets(@RequestParam(name = "festivalId") Long festivalId,
                                                    @RequestParam(name = "sortOption") SortOption sortOption,
                                                    @RequestParam(defaultValue="0") @Min(0) int page,
                                                    @RequestParam @Min(1) @Max(50) int size) {
    return upWidgetService.getUpWidgetsWithOffsetPaging(festivalId, sortOption, page, size);
  }

  @PatchMapping()
  public UpWidgetResDto update(@RequestParam("upWidgetId") Long upWidgetId,
                                       @RequestBody UpWidgetCreateDto widgetCreateDto) {
    return upWidgetService.update(upWidgetId, widgetCreateDto);
  }

  @DeleteMapping()
  public void delete(@RequestParam("upWidgetId") Long upWidgetId) {
    upWidgetService.delete(upWidgetId);
  }

  @GetMapping("/datetime")
  public List<UpWidgetResDto> getUpWidgetListByDateTime(
          @RequestParam("festivalId") Long festivalId,
          @RequestParam("now") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime now) {
    return upWidgetService.getUpWidgetsByNow(festivalId, now);
  }
}
