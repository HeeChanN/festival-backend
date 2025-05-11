package com.halo.eventer.domain.widget.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetResDto;
import com.halo.eventer.domain.widget.service.MainWidgetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mainWidgets")
@Tag(name = "메인 위젯 API")
public class MainWidgetController {

    private final MainWidgetService mainWidgetService;

    @PostMapping()
    public MainWidgetResDto create(
            @RequestParam Long festivalId, @RequestBody MainWidgetCreateDto mainWidgetCreateDto) {
        return mainWidgetService.create(festivalId, mainWidgetCreateDto);
    }

    @GetMapping()
    public List<MainWidgetResDto> getDownWidgets(@RequestParam("festivalId") Long festivalId) {
        return mainWidgetService.getAllMainWidget(festivalId);
    }

    @PatchMapping()
    public MainWidgetResDto update(@RequestParam Long id, @RequestBody MainWidgetCreateDto mainWidgetCreateDto) {
        return mainWidgetService.update(id, mainWidgetCreateDto);
    }

    @DeleteMapping()
    public void delete(@RequestParam Long id) {
        mainWidgetService.delete(id);
    }
}
