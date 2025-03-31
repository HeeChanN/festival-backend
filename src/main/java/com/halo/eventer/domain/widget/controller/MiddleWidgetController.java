package com.halo.eventer.domain.widget.controller;

import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.domain.widget.dto.middle_widget.*;
import com.halo.eventer.domain.widget.service.MiddleWidgetService;
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
@Validated
@RequestMapping("/api/middleWidget")
@Tag(name = "중간 위젯 API")
public class MiddleWidgetController {
  private final MiddleWidgetService middleWidgetService;

  @PostMapping()
  public MiddleWidgetResDto create(@RequestParam("festivalId") Long festivalId,
                                               @RequestBody MiddleWidgetCreateDto middleWidgetCreateDto) {
    return middleWidgetService.create(festivalId, middleWidgetCreateDto);
  }

  @GetMapping()
  public MiddleWidgetResDto getMiddleWidget(@RequestParam("id") Long id) {
    return middleWidgetService.getMiddleWidget(id);
  }

  @GetMapping("/{festivalId}")
  public PagedResponse<MiddleWidgetResDto> getMiddleWidgets(@PathVariable Long festivalId,
                                                            @RequestParam(name = "sortOption") SortOption sortOption,
                                                            @RequestParam(defaultValue="0") @Min(0) int page,
                                                            @RequestParam @Min(1) @Max(50) int size) {
    return middleWidgetService.getMiddleWidgetsWithOffsetPaging(festivalId,sortOption,page,size);
  }

  @PatchMapping()
  public MiddleWidgetResDto updateMiddleBanner(@RequestParam("id") Long id,
                                               @RequestBody MiddleWidgetCreateDto middleWidgetCreateDto) {
    return middleWidgetService.update(id, middleWidgetCreateDto);
  }

  @DeleteMapping()
  public void deleteMiddleBanner(@RequestParam("id") Long id) {
    middleWidgetService.delete(id);
  }

  @PatchMapping("/displayOrder")
  public List<MiddleWidgetResDto> updateDisplayOrder(@RequestParam Long festivalId,
                                                     @RequestBody List<OrderUpdateRequest> orderRequests) {
    return middleWidgetService.updateDisplayOrder(festivalId, orderRequests);
  }
}
