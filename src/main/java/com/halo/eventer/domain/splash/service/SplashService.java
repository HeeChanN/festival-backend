package com.halo.eventer.domain.splash.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.splash.Splash;
import com.halo.eventer.domain.splash.dto.DeleteImageDto;
import com.halo.eventer.domain.splash.dto.ImageLayerDto;
import com.halo.eventer.domain.splash.dto.UploadImageDto;
import com.halo.eventer.domain.splash.exception.SplashNotFoundException;
import com.halo.eventer.domain.splash.repository.SplashRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SplashService {
    private final FestivalRepository festivalRepository;
    private final SplashRepository splashRepository;

    /** 이미지 업로드 + 수정 */
    @Transactional
    public String uploadSplashImage(Long festivalId, UploadImageDto uploadImageDto) {
        Festival festival =
                festivalRepository.findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));

        Splash splash = splashRepository.findByFestivalId(festivalId).orElse(new Splash(festival));
        for (ImageLayerDto layer : uploadImageDto.getImageLayers()) {
            switch (layer.getLayerType()) {
                case "background":
                    splash.setBackgroundImage(layer.getUrl());
                    break;
                case "top":
                    splash.setTopLayerImage(layer.getUrl());
                    break;
                case "center":
                    splash.setCenterLayerImage(layer.getUrl());
                    break;
                case "bottom":
                    splash.setBottomLayerImage(layer.getUrl());
                    break;
            }
        }

        splashRepository.save(splash);
        return "이미지 업로드 성공";
    }

    /** 이미지 삭제 */
    @Transactional
    public String deleteSplashImage(Long festivalId, DeleteImageDto deleteImageDto) {
        Festival festival =
                festivalRepository.findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));

        Splash splash = splashRepository.findByFestivalId(festivalId).orElse(new Splash(festival));
        for (String layer : deleteImageDto.getLayerTypes()) {
            switch (layer) {
                case "background":
                    splash.setBackgroundImage(null);
                    break;
                case "top":
                    splash.setTopLayerImage(null);
                    break;
                case "center":
                    splash.setCenterLayerImage(null);
                    break;
                case "bottom":
                    splash.setBottomLayerImage(null);
                    break;
            }
        }

        splashRepository.save(splash);
        return "이미지 삭제 성공";
    }

    /** 전체 레이어 조회 */
    public Splash getSplash(Long festivalId) {
        Festival festival =
                festivalRepository.findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));
        Splash splash = splashRepository.findByFestivalId(festivalId).orElseThrow(SplashNotFoundException::new);
        return splash;
    }
}
