package com.halo.eventer.domain.duration.controller;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.duration.dto.DurationCreateDto;
import com.halo.eventer.domain.duration.dto.DurationResDto;
import com.halo.eventer.domain.duration.service.DurationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/duration")
public class DurationController {
    private final DurationService durationService;

    @PostMapping()
    public void createDurations(
            @Min(1) @RequestParam("festivalId") long festivalId,
            @RequestBody List<@Valid DurationCreateDto> durationCreateDtos) {
        durationService.createDurations(festivalId, durationCreateDtos);
    }

    @GetMapping()
    public List<DurationResDto> getDurations(@Min(1) @RequestParam("festivalId") Long festivalId) {
        return durationService.getDurations(festivalId);
    }
}
