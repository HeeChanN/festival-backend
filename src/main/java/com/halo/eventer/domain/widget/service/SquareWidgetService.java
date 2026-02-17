package com.halo.eventer.domain.widget.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.home.cache.HomeCacheEvictEvent;
import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.WidgetType;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetResDto;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.properties.SquareWidgetProperties;
import com.halo.eventer.domain.widget.repository.WidgetRepository;
import com.halo.eventer.domain.widget.util.WidgetPageHelper;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.sort.SortOption;
import com.halo.eventer.global.util.DisplayOrderUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SquareWidgetService {

    private final WidgetRepository widgetRepository;
    private final FestivalRepository festivalRepository;
    private final WidgetPageHelper widgetPageHelper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public SquareWidgetResDto create(Long festivalId, SquareWidgetCreateDto squareWidgetCreateDto) {
        Festival festival =
                festivalRepository.findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));

        Widget widget = widgetRepository.save(Widget.createSquareWidget(
                festival,
                squareWidgetCreateDto.getName(),
                squareWidgetCreateDto.getUrl(),
                squareWidgetCreateDto.getImage(),
                squareWidgetCreateDto.getDescription(),
                com.halo.eventer.global.constants.DisplayOrderConstants.DISPLAY_ORDER_DEFAULT));
        eventPublisher.publishEvent(new HomeCacheEvictEvent(festivalId));
        return SquareWidgetResDto.from(widget);
    }

    @Transactional(readOnly = true)
    public SquareWidgetResDto getSquareWidget(Long id) {
        Widget widget =
                widgetRepository.findById(id).orElseThrow(() -> new WidgetNotFoundException(id, WidgetType.SQUARE));

        return SquareWidgetResDto.from(widget);
    }

    @Transactional(readOnly = true)
    public PagedResponse<SquareWidgetResDto> getSquareWidgetsWithOffsetPaging(
            Long festivalId, SortOption sortOption, int page, int size) {
        validateFestival(festivalId);

        Pageable pageable = PageRequest.of(page, size);
        Page<Widget> widgetPage =
                widgetPageHelper.findWidgetsBySort(WidgetType.SQUARE, festivalId, sortOption, pageable);

        return widgetPageHelper.getPagedResponse(widgetPage, SquareWidgetResDto::from);
    }

    @Transactional
    public SquareWidgetResDto update(Long id, SquareWidgetCreateDto squareWidgetCreateDto) {
        Widget widget =
                widgetRepository.findById(id).orElseThrow(() -> new WidgetNotFoundException(id, WidgetType.SQUARE));
        widget.updateBaseField(squareWidgetCreateDto.getName(), squareWidgetCreateDto.getUrl());
        widget.updateProperties(
                new SquareWidgetProperties(squareWidgetCreateDto.getImage(), squareWidgetCreateDto.getDescription()));
        eventPublisher.publishEvent(new HomeCacheEvictEvent(widget.getFestival().getId()));
        return SquareWidgetResDto.from(widget);
    }

    @Transactional
    public void delete(Long id) {
        Widget widget =
                widgetRepository.findById(id).orElseThrow(() -> new WidgetNotFoundException(id, WidgetType.SQUARE));
        Long festivalId = widget.getFestival().getId();
        widgetRepository.delete(widget);
        eventPublisher.publishEvent(new HomeCacheEvictEvent(festivalId));
    }

    @Transactional
    public List<SquareWidgetResDto> updateDisplayOrder(Long festivalId, List<OrderUpdateRequest> orderRequests) {
        List<Widget> widgets = widgetRepository.findAllByFestivalIdAndWidgetType(festivalId, WidgetType.SQUARE);

        DisplayOrderUtils.updateDisplayOrder(widgets, orderRequests);

        eventPublisher.publishEvent(new HomeCacheEvictEvent(festivalId));
        return widgets.stream().map(SquareWidgetResDto::from).collect(Collectors.toList());
    }

    private void validateFestival(Long festivalId) {
        if (!festivalRepository.existsById(festivalId)) {
            throw new FestivalNotFoundException(festivalId);
        }
    }
}
