package com.halo.eventer.domain.widget.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.WidgetType;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetResDto;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.properties.MainWidgetProperties;
import com.halo.eventer.domain.widget.repository.WidgetRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainWidgetService {

    private final FestivalRepository festivalRepository;
    private final WidgetRepository widgetRepository;

    @Transactional
    public MainWidgetResDto create(Long festivalId, MainWidgetCreateDto mainWidgetCreateDto) {
        Festival festival =
                festivalRepository.findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));

        Widget widget = widgetRepository.save(Widget.createMainWidget(
                festival,
                mainWidgetCreateDto.getName(),
                mainWidgetCreateDto.getUrl(),
                mainWidgetCreateDto.getImage(),
                mainWidgetCreateDto.getDescription()));

        return MainWidgetResDto.from(widget);
    }

    @Transactional(readOnly = true)
    public List<MainWidgetResDto> getAllMainWidget(Long festivalId) {
        return widgetRepository.findAllByFestivalIdAndWidgetType(festivalId, WidgetType.MAIN).stream()
                .map(MainWidgetResDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public MainWidgetResDto update(Long id, MainWidgetCreateDto mainWidgetCreateDto) {
        Widget widget =
                widgetRepository.findById(id).orElseThrow(() -> new WidgetNotFoundException(id, WidgetType.MAIN));
        widget.updateBaseField(mainWidgetCreateDto.getName(), mainWidgetCreateDto.getUrl());
        widget.updateProperties(
                new MainWidgetProperties(mainWidgetCreateDto.getImage(), mainWidgetCreateDto.getDescription()));
        return MainWidgetResDto.from(widget);
    }

    @Transactional
    public void delete(Long id) {
        widgetRepository.deleteById(id);
    }
}
