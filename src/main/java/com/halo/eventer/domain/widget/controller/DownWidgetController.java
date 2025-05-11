package com.halo.eventer.domain.widget.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetResDto;
import com.halo.eventer.domain.widget.service.DownWidgetService;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/downWidgets")
@Tag(name = "하단 위젯 API")
public class DownWidgetController {

    private final DownWidgetService downWidgetService;

    @PostMapping()
    public DownWidgetResDto create(
            @RequestParam Long festivalId, @RequestBody DownWidgetCreateDto downWidgetCreateDto) {
        return downWidgetService.create(festivalId, downWidgetCreateDto);
    }

    @GetMapping()
    public List<DownWidgetResDto> getDownWidgets(@RequestParam("festivalId") Long festivalId) {
        return downWidgetService.getAllDownWidgets(festivalId);
    }

    @PatchMapping()
    public DownWidgetResDto update(@RequestParam Long id, @RequestBody DownWidgetCreateDto downWidgetCreateDto) {
        return downWidgetService.update(id, downWidgetCreateDto);
    }

    @DeleteMapping()
    public void delete(@RequestParam Long id) {
        downWidgetService.delete(id);
    }

    @PatchMapping("/displayOrder")
    public List<DownWidgetResDto> updateDisplayOrder(
            @RequestParam long festivalId, @RequestBody List<OrderUpdateRequest> orderRequests) {
        return downWidgetService.updateDisplayOrder(festivalId, orderRequests);
    }
}
