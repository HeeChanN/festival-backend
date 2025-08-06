package com.halo.eventer.domain.splash.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.splash.Splash;
import com.halo.eventer.domain.splash.dto.request.ImageLayerDto;
import com.halo.eventer.domain.splash.dto.response.SplashGetDto;
import com.halo.eventer.domain.splash.exception.SplashNotFoundException;
import com.halo.eventer.domain.splash.repository.SplashRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SplashService {
    private final FestivalRepository festivalRepository;
    private final SplashRepository splashRepository;

    @Transactional
    public void uploadSplashImage(long festivalId, List<ImageLayerDto> dto) {
        Festival festival = loadFestivalOrThrow(festivalId);
        Splash splash = loadSplashOrThrow(festivalId);
        // TODO : 지정된 레이어 값을 벗어난 값들이 들어올 때의 예외 처리 필요. enum?
        for (ImageLayerDto layer : dto) {
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
    }

    @Transactional
    public void deleteSplashImage(long festivalId, List<String> layerTypes) {
        Festival festival = loadFestivalOrThrow(festivalId);
        Splash splash = loadSplashOrThrow(festivalId);
        for (String layer : layerTypes) {
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
    }

    public SplashGetDto getSplash(long festivalId) {
        loadFestivalOrThrow(festivalId);
        Splash splash = loadSplashOrThrow(festivalId);
        return new SplashGetDto(
                splash.getBackgroundImage(),
                splash.getTopLayerImage(),
                splash.getCenterLayerImage(),
                splash.getBottomLayerImage());
    }

    private Splash loadSplashOrThrow(long festivalId) {
        return splashRepository.findByFestivalId(festivalId).orElseThrow(SplashNotFoundException::new);
    }

    private Festival loadFestivalOrThrow(long festivalId) {
        return festivalRepository.findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));
    }
}
