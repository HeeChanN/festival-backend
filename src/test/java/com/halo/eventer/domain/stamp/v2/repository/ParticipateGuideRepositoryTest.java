package com.halo.eventer.domain.stamp.v2.repository;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.stamp.ParticipateGuide;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.repository.ParticipateGuideRepository;
import com.halo.eventer.domain.stamp.repository.StampRepository;
import com.halo.eventer.domain.stamp.v2.fixture.ParticipateGuideFixture;
import com.halo.eventer.domain.stamp.v2.fixture.StampTourFixture;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class ParticipateGuideRepositoryTest {

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private StampRepository stampRepository;

    @Autowired
    private ParticipateGuideRepository participateGuideRepository;

    private Festival 축제;
    private Stamp 스탬프;
    private ParticipateGuide 참여가이드;

    @BeforeEach
    void setUp() {
        축제 = FestivalFixture.축제_엔티티();
        스탬프 = StampTourFixture.스탬프투어1_생성(축제);
        참여가이드 = ParticipateGuideFixture.참여가이드_기본값(스탬프);

        festivalRepository.save(축제);
        stampRepository.save(스탬프);
        participateGuideRepository.save(참여가이드);
    }

    @Test
    void 스탬프ID로_참여가이드_조회() {
        // when
        Optional<ParticipateGuide> result = participateGuideRepository.findFirstByStampId(스탬프.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getStamp().getId()).isEqualTo(스탬프.getId());
    }
}
