package com.halo.eventer.domain.widget.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.home.cache.HomeCacheEvictEvent;
import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.WidgetType;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetResDto;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.repository.WidgetRepository;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.global.util.DisplayOrderUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DownWidgetService {

    private final WidgetRepository widgetRepository;
    private final FestivalRepository festivalRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public DownWidgetResDto create(Long festivalId, DownWidgetCreateDto downWidgetCreateDto) {
        Festival festival =
                festivalRepository.findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));

        Widget widget = widgetRepository.save(Widget.createDownWidget(
                festival,
                downWidgetCreateDto.getName(),
                downWidgetCreateDto.getUrl(),
                com.halo.eventer.global.constants.DisplayOrderConstants.DISPLAY_ORDER_DEFAULT));

        eventPublisher.publishEvent(new HomeCacheEvictEvent(festivalId));
        return DownWidgetResDto.from(widget);
    }

    @Transactional(readOnly = true)
    public List<DownWidgetResDto> getAllDownWidgets(Long festivalId) {
        return widgetRepository.findAllByFestivalIdAndWidgetType(festivalId, WidgetType.DOWN).stream()
                .map(DownWidgetResDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public DownWidgetResDto update(Long id, DownWidgetCreateDto downWidgetCreateDto) {
        Widget widget =
                widgetRepository.findById(id).orElseThrow(() -> new WidgetNotFoundException(id, WidgetType.DOWN));
        widget.updateBaseField(downWidgetCreateDto.getName(), downWidgetCreateDto.getUrl());
        eventPublisher.publishEvent(new HomeCacheEvictEvent(widget.getFestival().getId()));
        return DownWidgetResDto.from(widget);
    }

    @Transactional
    public void delete(Long id) {
        Widget widget =
                widgetRepository.findById(id).orElseThrow(() -> new WidgetNotFoundException(id, WidgetType.DOWN));
        Long festivalId = widget.getFestival().getId();
        widgetRepository.delete(widget);
        eventPublisher.publishEvent(new HomeCacheEvictEvent(festivalId));
    }

    @Transactional
    public List<DownWidgetResDto> updateDisplayOrder(Long festivalId, List<OrderUpdateRequest> orderRequests) {
        List<Widget> widgets = widgetRepository.findAllByFestivalIdAndWidgetType(festivalId, WidgetType.DOWN);

        DisplayOrderUtils.updateDisplayOrder(widgets, orderRequests);

        eventPublisher.publishEvent(new HomeCacheEvictEvent(festivalId));
        return widgets.stream().map(DownWidgetResDto::from).collect(Collectors.toList());
    }
}
