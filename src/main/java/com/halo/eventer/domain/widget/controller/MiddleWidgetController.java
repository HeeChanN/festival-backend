package com.halo.eventer.domain.widget.controller;

import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.widget.dto.middle_widget.*;
import com.halo.eventer.domain.widget.service.MiddleWidgetService;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.sort.SortOption;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/middleWidgets")
@Tag(name = "중간 위젯 API")
public class MiddleWidgetController {
    private final MiddleWidgetService middleWidgetService;

    @PostMapping()
    public MiddleWidgetResDto create(
            @RequestParam("festivalId") Long festivalId, @RequestBody MiddleWidgetCreateDto middleWidgetCreateDto) {
        return middleWidgetService.create(festivalId, middleWidgetCreateDto);
    }

    @GetMapping("/{id}")
    public MiddleWidgetResDto getMiddleWidget(@PathVariable("id") Long id) {
        return middleWidgetService.getMiddleWidget(id);
    }

    @GetMapping()
    public PagedResponse<MiddleWidgetResDto> getMiddleWidgets(
            @RequestParam("festivalId") Long festivalId,
            @RequestParam(name = "sortOption") SortOption sortOption,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam @Min(1) @Max(50) int size) {
        return middleWidgetService.getMiddleWidgetsWithOffsetPaging(festivalId, sortOption, page, size);
    }

    @PatchMapping()
    public MiddleWidgetResDto update(
            @RequestParam("id") Long id, @RequestBody MiddleWidgetCreateDto middleWidgetCreateDto) {
        return middleWidgetService.update(id, middleWidgetCreateDto);
    }

    @DeleteMapping()
    public void delete(@RequestParam("id") Long id) {
        middleWidgetService.delete(id);
    }

    @PatchMapping("/displayOrder")
    public List<MiddleWidgetResDto> updateDisplayOrder(
            @RequestParam Long festivalId, @RequestBody List<OrderUpdateRequest> orderRequests) {
        return middleWidgetService.updateDisplayOrder(festivalId, orderRequests);
    }
}
