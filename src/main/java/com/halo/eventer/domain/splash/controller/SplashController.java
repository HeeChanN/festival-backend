package com.halo.eventer.domain.splash.controller;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.splash.Splash;
import com.halo.eventer.domain.splash.dto.DeleteImageDto;
import com.halo.eventer.domain.splash.dto.SplashGetDto;
import com.halo.eventer.domain.splash.dto.UploadImageDto;
import com.halo.eventer.domain.splash.service.SplashService;
import com.halo.eventer.domain.splash.swagger.SplashGetApi;
import com.halo.eventer.domain.splash.swagger.SplashImageDeleteApi;
import com.halo.eventer.domain.splash.swagger.SplashImageUploadApi;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/splash")
public class SplashController {
    private final SplashService splashService;

    /** 이미지 업로드 + 수정 */
    @SplashImageUploadApi
    @PostMapping
    public String uploadSplashImage(@RequestParam Long festivalId, @RequestBody UploadImageDto dto) {
        return splashService.uploadSplashImage(festivalId, dto);
    }

    /** 이미지 삭제 */
    @SplashImageDeleteApi
    @DeleteMapping
    public String deleteSplashImage(@RequestParam Long festivalId, @RequestBody DeleteImageDto dto) {
        return splashService.deleteSplashImage(festivalId, dto);
    }

    /** 전체 레이어 조회 */
    @SplashGetApi
    @GetMapping
    public SplashGetDto getSplash(@RequestParam Long festivalId) {
        Splash splash = splashService.getSplash(festivalId);
        return new SplashGetDto(
                splash.getBackgroundImage(),
                splash.getTopLayerImage(),
                splash.getCenterLayerImage(),
                splash.getBottomLayerImage());
    }
}
