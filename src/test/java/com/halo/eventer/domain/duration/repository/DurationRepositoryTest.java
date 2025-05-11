package com.halo.eventer.domain.duration.repository;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.halo.eventer.domain.duration.Duration;
import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.dto.FestivalCreateDto;
import com.halo.eventer.domain.festival.repository.FestivalRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@Disabled()
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SuppressWarnings("NonAsciiCharacters")
public class DurationRepositoryTest {

    @Autowired
    private DurationRepository durationRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    private Festival festival;
    private Duration duration;

    @BeforeEach
    void setUp() {
        festival = Festival.from(new FestivalCreateDto("이름", "주소"));
        festival = festivalRepository.save(festival);
        duration = new Duration(LocalDate.of(2025, 3, 3), 3, festival);
        duration = durationRepository.save(duration);
    }

    @Test
    void duration_저장시_unique_제약조건으로_중복_방지() {
        // given
        Duration duplicateDuration = new Duration(LocalDate.of(2025, 3, 3), 3, festival);

        // when & then
        assertThatThrownBy(() -> {
                    durationRepository.save(duplicateDuration);
                })
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 축제id로_duration_전체조회() {
        // when
        List<Duration> durations = durationRepository.findAllByFestivalId(festival.getId());

        // then
        assertThat(durations.size()).isEqualTo(1);
    }

    @Test
    void durationId_리스트로_duration_전체조회() {
        // when
        List<Duration> durations = durationRepository.findAllByIdIn(List.of(duration.getId()));

        // then
        assertThat(durations.size()).isEqualTo(1);
    }
}
