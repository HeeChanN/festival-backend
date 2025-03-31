package com.halo.eventer.domain.widget.controller;

import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetResDto;
import com.halo.eventer.domain.widget.service.SquareWidgetService;
import com.halo.eventer.global.common.sort.SortOption;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/squareWidget")
@Validated
@Tag(name = "정사각형 위젯 API")
public class SquareWidgetController {

  private final SquareWidgetService squareWidgetService;

  @PostMapping("/{festivalId}")
  public SquareWidgetResDto create(@PathVariable Long festivalId,
                                   @RequestBody SquareWidgetCreateDto squareWidgetCreateDto) {

    return squareWidgetService.create(festivalId, squareWidgetCreateDto);
  }

  @GetMapping
  public SquareWidgetResDto getWidget(@RequestParam("widgetId") Long widgetId) {
    return squareWidgetService.getSquareWidget(widgetId);
  }

  @GetMapping("/{festivalId}")
  public PagedResponse<SquareWidgetResDto> getWidgets(@PathVariable Long festivalId,
                                                      @RequestParam(name = "sortOption") SortOption sortOption,
                                                      @RequestParam(defaultValue="0") @Min(0) int page,
                                                      @RequestParam @Min(1) @Max(50) int size) {
    return squareWidgetService.getSquareWidgetsWithOffsetPaging(festivalId, sortOption, page, size);
  }

  @PatchMapping
  public SquareWidgetResDto update(@RequestParam("widgetId") Long widgetId,
                                   @RequestBody SquareWidgetCreateDto squareWidgetCreateDto) {
    return squareWidgetService.update(widgetId, squareWidgetCreateDto);
  }

  @DeleteMapping
  public void delete(@RequestParam("id") Long id) {
    squareWidgetService.delete(id);
  }

  @PatchMapping("/displayOrder")
  public List<SquareWidgetResDto> updateDisplayOrder(@RequestParam long festivalId,
                                                     @RequestBody List<OrderUpdateRequest> orderRequests){
    return squareWidgetService.updateDisplayOrder(festivalId, orderRequests);
  }
}
