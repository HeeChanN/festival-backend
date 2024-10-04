package com.halo.eventer.domain.splash.controller;

import com.halo.eventer.domain.splash.Splash;
import com.halo.eventer.domain.splash.dto.DeleteImageDto;
import com.halo.eventer.domain.splash.dto.SplashGetDto;
import com.halo.eventer.domain.splash.dto.UploadImageDto;
import com.halo.eventer.domain.splash.service.SplashService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/splash")
public class SplashController {
    private final SplashService splashService;
    
    /** 이미지 업로드 + 수정 */
    @PostMapping
    public String uploadSplashImage(@RequestParam Long festivalId, @RequestBody UploadImageDto dto) {
        return splashService.uploadSplashImage(festivalId, dto);
    }

    /** 이미지 삭제 */
    @DeleteMapping
    public String deleteSplashImage(@RequestParam Long festivalId, @RequestBody DeleteImageDto dto) {
        return splashService.deleteSplashImage(festivalId, dto);
    }

    /** 전체 레이어 조회 */
    @GetMapping
    public SplashGetDto getSplash(@RequestParam Long festivalId) {
        Splash splash = splashService.getSplash(festivalId);
        return new SplashGetDto(splash.getBackgroundImage(), splash.getTopLayerImage(), splash.getCenterLayerImage(), splash.getBottomLayerImage());
    }
}
