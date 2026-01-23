package com.halo.eventer.domain.festival.controller;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.festival.dto.*;
import com.halo.eventer.domain.festival.service.FestivalService;
import com.halo.eventer.domain.image.dto.FileDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/festivals")
public class FestivalController {

    private final FestivalService festivalService;

    @PostMapping()
    public void createFestival(@Valid @RequestBody FestivalCreateDto festivalCreateDto) {
        festivalService.create(festivalCreateDto);
    }

    @GetMapping("/{id}")
    public FestivalResDto getFestival(@Min(1) @PathVariable("id") Long id) {
        return festivalService.findById(id);
    }

    @GetMapping
    public List<FestivalSummaryDto> getFestivals() {
        return festivalService.findAll();
    }

    @PutMapping()
    public FestivalResDto updateFestival(
            @Min(1) @RequestParam("id") Long id, @Valid @RequestBody FestivalCreateDto festivalCreateDto) {
        return festivalService.update(id, festivalCreateDto);
    }

    @DeleteMapping()
    public void deleteFestival(@Min(1) @RequestParam("id") Long id) {
        festivalService.delete(id);
    }

    @PatchMapping("/{festivalId}/color")
    public void addColor(@Min(1) @PathVariable("festivalId") Long festivalId, @Valid @RequestBody ColorDto colorDto) {
        festivalService.updateColor(festivalId, colorDto);
    }

    @PatchMapping("/{festivalId}/logo")
    public void addLogo(@Min(1) @PathVariable("festivalId") Long festivalId, @RequestBody FileDto fileDto) {
        festivalService.updateLogo(festivalId, fileDto);
    }

    @PatchMapping("/{festivalId}/location")
    public FestivalResDto updateFestivalLocation(
            @Min(1) @PathVariable("festivalId") Long festivalId, @RequestBody FestivalLocationDto festivalLocationDto) {
        return festivalService.updateLocation(festivalId, festivalLocationDto);
    }

    @GetMapping("/{festivalId}/location")
    public FestivalLocationDto getFestivalLocationInfo(@PathVariable("festivalId") Long festivalId) {
        return festivalService.getFestivalLocationInfo(festivalId);
    }

    @PatchMapping("/{festivalId}/name")
    public void updateFestivalName(
            @PathVariable("festivalId") Long festivalId, @RequestBody FestivalNameReqDto festivalNameReqDto) {
        festivalService.updateFestivalName(festivalId, festivalNameReqDto);
    }

    @PatchMapping("/{festivalId}/sub-domain")
    public void updateFestivalSubDomain(
            @PathVariable("festivalId") Long festivalId, @RequestBody FestivalSubDomainReqDto festivalSubdomainReqDto) {
        festivalService.updateFestivalSubDomain(festivalId, festivalSubdomainReqDto);
    }
}
