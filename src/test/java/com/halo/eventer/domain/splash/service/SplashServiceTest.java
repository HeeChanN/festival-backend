package com.halo.eventer.domain.splash.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.splash.Splash;
import com.halo.eventer.domain.splash.SplashFixture;
import com.halo.eventer.domain.splash.dto.response.SplashGetDto;
import com.halo.eventer.domain.splash.exception.SplashNotFoundException;
import com.halo.eventer.domain.splash.repository.SplashRepository;

import static com.halo.eventer.domain.splash.SplashFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class SplashServiceTest {

    @Mock
    private FestivalRepository festivalRepository;

    @Mock
    private SplashRepository splashRepository;

    @InjectMocks
    private SplashService splashService;

    private Festival festival;
    private Splash splash1;

    @BeforeEach
    void setup() {
        festival = FestivalFixture.축제_엔티티();
        splash1 = SplashFixture.스플래시1_생성(festival);
    }

    @Test
    void 스플레시_업로드() {
        // given
        given(festivalRepository.findById(anyLong())).willReturn(Optional.of(festival));
        given(splashRepository.findByFestivalId(anyLong())).willReturn(Optional.of(splash1));
        given(splashRepository.save(any(Splash.class))).willReturn(splash1);

        // when
        splashService.uploadSplashImage(1, 이미지_업로드_DTO_생성());

        // then
        assertThat(splash1.getBackgroundImage()).isEqualTo(배경_이미지_업로드_DTO().getUrl());
        assertThat(splash1.getTopLayerImage()).isEqualTo(상단_이미지_업로드_DTO().getUrl());
        assertThat(splash1.getCenterLayerImage()).isEqualTo(중앙_이미지_업로드_DTO().getUrl());
        assertThat(splash1.getBottomLayerImage()).isEqualTo(하단_이미지_업로드_DTO().getUrl());
    }

    @Test
    void 스플레시_업로드_축제_찾을수없음() {
        // given
        given(festivalRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> splashService.uploadSplashImage(1L, List.of()))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 스플레시_업로드_스플레시_찾을수없음() {
        // given
        given(festivalRepository.findById(anyLong())).willReturn(Optional.of(festival));
        given(splashRepository.findByFestivalId(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> splashService.uploadSplashImage(1L, List.of()))
                .isInstanceOf(SplashNotFoundException.class);
    }

    @Test
    void 스플레시_이미지_삭제() {
        // given
        given(festivalRepository.findById(anyLong())).willReturn(Optional.of(festival));
        given(splashRepository.findByFestivalId(anyLong())).willReturn(Optional.of(splash1));

        // when
        splashService.deleteSplashImage(1, 이미지_삭제_타입_리스트());

        // then
        assertThat(splash1.getBackgroundImage()).isEqualTo(null);
        assertThat(splash1.getTopLayerImage()).isEqualTo(null);
        assertThat(splash1.getCenterLayerImage()).isEqualTo(null);
        assertThat(splash1.getBottomLayerImage()).isEqualTo(null);
    }

    @Test
    void 스플레시_이미지_삭제_축제_찾을수없음() {
        // given
        given(festivalRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> splashService.deleteSplashImage(1L, List.of()))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 스플레시_이미지_삭제_스플레시_찾을수없음() {
        // given
        given(festivalRepository.findById(anyLong())).willReturn(Optional.of(festival));
        given(splashRepository.findByFestivalId(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> splashService.deleteSplashImage(1L, List.of()))
                .isInstanceOf(SplashNotFoundException.class);
    }

    @Test
    void 스플레시_조회() {
        // given
        given(festivalRepository.findById(anyLong())).willReturn(Optional.of(festival));
        given(splashRepository.findByFestivalId(anyLong())).willReturn(Optional.of(splash1));

        // when
        SplashGetDto result = splashService.getSplash(1);

        // then
        assertThat(result.getBackground()).isEqualTo(splash1.getBackgroundImage());
        assertThat(result.getTop()).isEqualTo(splash1.getTopLayerImage());
        assertThat(result.getCenter()).isEqualTo(splash1.getCenterLayerImage());
        assertThat(result.getBottom()).isEqualTo(splash1.getBottomLayerImage());
    }

    @Test
    void 스플레시_조회_축제_찾을수없음() {
        // given
        given(festivalRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> splashService.getSplash(1L)).isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 스플레시_조회_스플레시_찾을수없음() {
        // given
        given(festivalRepository.findById(anyLong())).willReturn(Optional.of(festival));
        given(splashRepository.findByFestivalId(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> splashService.getSplash(1L)).isInstanceOf(SplashNotFoundException.class);
    }
}
