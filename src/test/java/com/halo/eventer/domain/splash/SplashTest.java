package com.halo.eventer.domain.splash;

import org.junit.jupiter.api.BeforeEach;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;

@SuppressWarnings("NonAsciiCharacters")
public class SplashTest {

    private Festival festival;

    @BeforeEach
    void setUp() {
        festival = FestivalFixture.축제_엔티티();
    }
}
