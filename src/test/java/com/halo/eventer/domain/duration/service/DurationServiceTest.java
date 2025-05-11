package com.halo.eventer.domain.duration.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.duration.Duration;
import com.halo.eventer.domain.duration.dto.DurationCreateDto;
import com.halo.eventer.domain.duration.dto.DurationResDto;
import com.halo.eventer.domain.duration.exception.DurationDateAlreadyExistsException;
import com.halo.eventer.domain.duration.repository.DurationRepository;
import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.dto.FestivalCreateDto;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class DurationServiceTest {

    @Mock
    private FestivalRepository festivalRepository;

    @Mock
    private DurationRepository durationRepository;

    @InjectMocks
    private DurationService durationService;

    private Festival festival;
    List<Duration> durations;
    List<Duration> duplicatedDurations;
    List<DurationCreateDto> durationDtos;
    private final Long festivalId = 1L;

    @BeforeEach
    void setUp() {
        festival = Festival.from(new FestivalCreateDto("이름", "주소"));
        durations = List.of(Duration.of(festival, new DurationCreateDto(LocalDate.of(2025, 3, 2), 3)));
        duplicatedDurations = List.of(
                Duration.of(festival, new DurationCreateDto(LocalDate.of(2025, 3, 1), 3)),
                Duration.of(festival, new DurationCreateDto(LocalDate.of(2025, 3, 5), 3)));
        durationDtos = List.of(
                new DurationCreateDto(LocalDate.of(2025, 3, 1), 3), new DurationCreateDto(LocalDate.of(2025, 3, 5), 2));
    }

    @Test
    void 정상적으로_durations_생성() {
        // given
        given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
        given(durationRepository.findAllByFestivalId(festivalId)).willReturn(durations);

        // when
        durationService.createDurations(festivalId, durationDtos);

        // then
        verify(festivalRepository, times(1)).findById(festivalId);
        verify(durationRepository, times(1)).findAllByFestivalId(festivalId);
        verify(durationRepository, times(2)).save(any());
    }

    @Test
    void 축제가_없을때_예외() {
        // given
        given(festivalRepository.findById(festivalId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> durationService.createDurations(festivalId, durationDtos))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 이미_해당날짜가_존재할때() {
        // given
        given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
        given(durationRepository.findAllByFestivalId(festivalId)).willReturn(duplicatedDurations);

        // when & then
        assertThatThrownBy(() -> durationService.createDurations(festivalId, durationDtos))
                .isInstanceOf(DurationDateAlreadyExistsException.class);
    }

    @Test
    void 축제_기간_전체조회() {
        // given
        given(durationRepository.findAllByFestivalId(festivalId)).willReturn(durations);

        // when
        List<DurationResDto> targets = durationService.getDurations(festivalId);

        // then
        assertThat(targets.size()).isEqualTo(1);
        assertThat(targets.get(0).getDate()).isEqualTo("2025-03-02");
        assertThat(targets.get(0).getDay()).isEqualTo(3);
    }
}
