package com.halo.eventer.domain.splash.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.festival.dto.FestivalCreateDto;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.splash.Splash;
import com.halo.eventer.domain.splash.SplashFixture;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class SplashRepositoryTest {

    @Autowired
    private SplashRepository splashRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    private FestivalCreateDto dto;
    private Splash splash;

    @BeforeEach
    void setUp() {
        dto = FestivalFixture.축제_생성용_DTO();
        Festival festival = festivalRepository.save(FestivalFixture.축제_엔티티());
        splash = SplashFixture.스플래시1_생성(festival);
        splashRepository.save(splash);
    }

    @Test
    void 축제id로_스플래시_찾기() {
        // given
        Festival festival = festivalRepository.findByName(dto.getName()).get();

        // when
        Splash result = splashRepository.findByFestivalId(festival.getId()).get();

        // then
        assertThat(result.getId()).isEqualTo(splash.getId());
        assertThat(result.getBackgroundImage()).isEqualTo(splash.getBackgroundImage());
        assertThat(result.getTopLayerImage()).isEqualTo(splash.getTopLayerImage());
        assertThat(result.getCenterLayerImage()).isEqualTo(splash.getCenterLayerImage());
        assertThat(result.getBottomLayerImage()).isEqualTo(splash.getBottomLayerImage());
    }
}
