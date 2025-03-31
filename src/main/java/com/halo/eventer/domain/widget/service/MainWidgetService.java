package com.halo.eventer.domain.widget.service;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetResDto;
import com.halo.eventer.domain.widget.entity.MainWidget;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.repository.DownWidgetRepository;
import com.halo.eventer.domain.widget.repository.MainWidgetRepository;
import com.halo.eventer.domain.widget.WidgetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MainWidgetService {

    private final DownWidgetRepository downWidgetRepository;
    private final FestivalRepository festivalRepository;
    private final MainWidgetRepository mainWidgetRepository;

    @Transactional
    public MainWidgetResDto create(Long festivalId, MainWidgetCreateDto mainWidgetCreateDto) {
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new FestivalNotFoundException(festivalId));

        MainWidget mainWidget = mainWidgetRepository.save(MainWidget.from(festival, mainWidgetCreateDto));

        return MainWidgetResDto.from(mainWidget);
    }

    @Transactional(readOnly = true)
    public List<MainWidgetResDto> getAllMainWidget(Long festivalId) {
        return mainWidgetRepository.findAllByFestivalId(festivalId).stream()
                .map(MainWidgetResDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public MainWidgetResDto update(Long id, MainWidgetCreateDto mainWidgetCreateDto) {
        MainWidget mainWidget = mainWidgetRepository.findById(id)
                .orElseThrow(()->new WidgetNotFoundException(id, WidgetType.MAIN));
        mainWidget.updateMainWidget(mainWidgetCreateDto);
        return MainWidgetResDto.from(mainWidget);
    }

    @Transactional
    public void delete(Long id) {
        downWidgetRepository.deleteById(id);
    }
}
