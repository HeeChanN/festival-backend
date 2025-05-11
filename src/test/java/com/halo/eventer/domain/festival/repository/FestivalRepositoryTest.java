package com.halo.eventer.domain.festival.repository;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.dto.FestivalCreateDto;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Disabled()
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SuppressWarnings("NonAsciiCharacters")
public class FestivalRepositoryTest {

    @Autowired
    private FestivalRepository festivalRepository;

    private Festival festival;

    @BeforeEach
    void setUp() {
        FestivalCreateDto dto = new FestivalCreateDto("축제", "univ");
        festival = festivalRepository.save(Festival.from(dto));
    }

    @Test
    void 축제생성() {
        assertThat(festival.getName()).isEqualTo("축제");
        assertThat(festival.getSubAddress()).isEqualTo("univ");
    }

    @Test
    void 축제이름으로_축제_찾기() {
        // when
        Optional<Festival> result = festivalRepository.findByName("축제");

        // then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(festival);
    }

    @Test
    void 축제이름으로_축제_찾기_실패() {
        // when
        Optional<Festival> result = festivalRepository.findByName("축");

        // then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void subAddress로_축제_찾기() {
        // when
        Optional<Festival> result = festivalRepository.findBySubAddress("univ");

        // then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(festival);
    }

    @Test
    void subAddress로_축제_찾기_실패() {
        // when
        Optional<Festival> result = festivalRepository.findBySubAddress("uni");

        // then
        assertThat(result.isPresent()).isFalse();
    }
}
