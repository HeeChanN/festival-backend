package com.halo.eventer.domain.widget.controller;

import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetResDto;
import com.halo.eventer.domain.widget.service.SquareWidgetService;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.sort.SortOption;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/squareWidgets")
@Validated
@Tag(name = "정사각형 위젯 API")
public class SquareWidgetController {

    private final SquareWidgetService squareWidgetService;

    @PostMapping()
    public SquareWidgetResDto create(
            @RequestParam("festivalId") Long festivalId, @RequestBody SquareWidgetCreateDto squareWidgetCreateDto) {

        return squareWidgetService.create(festivalId, squareWidgetCreateDto);
    }

    @GetMapping("/{id}")
    public SquareWidgetResDto getWidget(@PathVariable("id") Long id) {
        return squareWidgetService.getSquareWidget(id);
    }

    @GetMapping()
    public PagedResponse<SquareWidgetResDto> getWidgets(
            @RequestParam("festivalId") Long festivalId,
            @RequestParam(name = "sortOption") SortOption sortOption,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam @Min(1) @Max(50) int size) {
        return squareWidgetService.getSquareWidgetsWithOffsetPaging(festivalId, sortOption, page, size);
    }

    @PatchMapping
    public SquareWidgetResDto update(
            @RequestParam("id") Long id, @RequestBody SquareWidgetCreateDto squareWidgetCreateDto) {
        return squareWidgetService.update(id, squareWidgetCreateDto);
    }

    @DeleteMapping
    public void delete(@RequestParam("id") Long id) {
        squareWidgetService.delete(id);
    }

    @PatchMapping("/displayOrder")
    public List<SquareWidgetResDto> updateDisplayOrder(
            @RequestParam long festivalId, @RequestBody List<OrderUpdateRequest> orderRequests) {
        return squareWidgetService.updateDisplayOrder(festivalId, orderRequests);
    }
}
