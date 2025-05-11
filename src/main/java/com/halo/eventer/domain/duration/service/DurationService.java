package com.halo.eventer.domain.duration.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.duration.Duration;
import com.halo.eventer.domain.duration.dto.DurationCreateDto;
import com.halo.eventer.domain.duration.dto.DurationResDto;
import com.halo.eventer.domain.duration.exception.DurationDateAlreadyExistsException;
import com.halo.eventer.domain.duration.repository.DurationRepository;
import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DurationService {
    private final DurationRepository durationRepository;
    private final FestivalRepository festivalRepository;

    @Transactional
    public void createDurations(Long festivalId, List<DurationCreateDto> durationCreateDtos) {
        Festival festival =
                festivalRepository.findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));

        List<Duration> durations = durationRepository.findAllByFestivalId(festivalId);
        checkDuplicatedDates(durationCreateDtos, durations);

        durationCreateDtos.forEach(dto -> durationRepository.save(Duration.of(festival, dto)));
    }

    @Transactional(readOnly = true)
    public List<DurationResDto> getDurations(Long festivalId) {
        List<Duration> durations = durationRepository.findAllByFestivalId(festivalId);
        return DurationResDto.fromDurations(durations);
    }

    private void checkDuplicatedDates(List<DurationCreateDto> dtos, List<Duration> existingDurations) {
        Set<LocalDate> existingDates =
                existingDurations.stream().map(Duration::getDate).collect(Collectors.toSet());

        boolean hasDuplicate = dtos.stream().anyMatch(newDto -> existingDates.contains(newDto.getDate()));

        if (hasDuplicate) {
            throw new DurationDateAlreadyExistsException("이미 동일한 날짜가 존재합니다.");
        }
    }
}
