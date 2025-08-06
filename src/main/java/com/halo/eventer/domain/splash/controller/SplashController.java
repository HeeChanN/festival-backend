package com.halo.eventer.domain.splash.controller;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.splash.dto.request.ImageLayerDto;
import com.halo.eventer.domain.splash.dto.response.SplashGetDto;
import com.halo.eventer.domain.splash.service.SplashService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/splash")
public class SplashController {
    private final SplashService splashService;

    @PostMapping
    public void uploadSplashImage(@Min(1) @RequestParam long festivalId, @RequestBody List<@Valid ImageLayerDto> dto) {
        splashService.uploadSplashImage(festivalId, dto);
    }

    @DeleteMapping
    public void deleteSplashImage(
            @Min(1) @RequestParam long festivalId, @RequestBody List<@NotBlank String> layerTypes) {
        splashService.deleteSplashImage(festivalId, layerTypes);
    }

    @GetMapping
    public SplashGetDto getSplash(@Min(1) @RequestParam long festivalId) {
        return splashService.getSplash(festivalId);
    }
}
