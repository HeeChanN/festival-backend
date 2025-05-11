package com.halo.eventer.domain.widget_item.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.image.dto.FileDto;
import com.halo.eventer.domain.image.dto.ImageDto;
import com.halo.eventer.domain.widget_item.dto.WidgetItemCreateDto;
import com.halo.eventer.domain.widget_item.dto.WidgetItemResDto;
import com.halo.eventer.domain.widget_item.service.WidgetItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/widgetItem")
@Tag(name = "위젯 내부 요소 (메인 위젯에 연결되는 입장방법, 관람안내 등)")
public class WidgetItemController {
    private final WidgetItemService widgetItemService;

    @PostMapping("/{widgetId}")
    public WidgetItemResDto createInfoName(
            @PathVariable("widgetId") Long widgetId, @RequestBody WidgetItemCreateDto widgetItemCreateDto) {
        return widgetItemService.create(widgetId, widgetItemCreateDto);
    }

    @GetMapping("/{widgetId}")
    public List<WidgetItemResDto> getWidgetItemsByWidgetId(@PathVariable("widgetId") Long widgetId) {
        return widgetItemService.getWidgetItemWithWidgetId(widgetId);
    }

    @GetMapping
    public List<ImageDto> getWidgetItemImages(@RequestParam("widgetId") Long widgetId) {
        return widgetItemService.getWidgetItemImages(widgetId);
    }

    @PatchMapping("/{widgetId}")
    public WidgetItemResDto updateWidgetItem(
            @PathVariable("widgetId") Long id, @RequestBody WidgetItemCreateDto widgetItemCreateDto) {
        return widgetItemService.updateWidgetInfo(id, widgetItemCreateDto);
    }

    @PostMapping("/{widgetId}/images")
    public List<ImageDto> addImages(@PathVariable Long widgetId, @RequestBody List<FileDto> fileDtos) {
        return widgetItemService.addImages(widgetId, fileDtos);
    }

    @DeleteMapping("/{widgetId}/images")
    public void deleteImages(@PathVariable Long widgetId, @RequestBody List<Long> imageIds) {
        widgetItemService.deleteImages(widgetId, imageIds);
    }

    @DeleteMapping("/{widgetId}")
    public void deleteConcertInfo(@PathVariable("widgetId") Long id) {
        widgetItemService.delete(id);
    }
}
