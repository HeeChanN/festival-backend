package com.halo.eventer.domain.widget.service;

import java.time.LocalDateTime;
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
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetResDto;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.properties.UpWidgetProperties;
import com.halo.eventer.domain.widget.repository.WidgetRepository;
import com.halo.eventer.domain.widget.util.WidgetPageHelper;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.sort.SortOption;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpWidgetService {

    private final WidgetRepository widgetRepository;
    private final FestivalRepository festivalRepository;
    private final WidgetPageHelper widgetPageHelper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public UpWidgetResDto create(Long festivalId, UpWidgetCreateDto upWidgetCreateDto) {
        Festival festival =
                festivalRepository.findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));

        Widget widget = widgetRepository.save(Widget.createUpWidget(
                festival,
                upWidgetCreateDto.getName(),
                upWidgetCreateDto.getUrl(),
                upWidgetCreateDto.getPeriodStart(),
                upWidgetCreateDto.getPeriodEnd()));
        eventPublisher.publishEvent(new HomeCacheEvictEvent(festivalId));
        return UpWidgetResDto.from(widget);
    }

    @Transactional(readOnly = true)
    public UpWidgetResDto getUpWidget(Long id) {
        Widget widget = widgetRepository.findById(id).orElseThrow(() -> new WidgetNotFoundException(id, WidgetType.UP));

        return UpWidgetResDto.from(widget);
    }

    @Transactional(readOnly = true)
    public PagedResponse<UpWidgetResDto> getUpWidgetsWithOffsetPaging(
            Long festivalId, SortOption sortOption, int page, int size) {
        validateFestival(festivalId);

        Pageable pageable = PageRequest.of(page, size);
        Page<Widget> widgetPage = widgetPageHelper.findWidgetsBySort(WidgetType.UP, festivalId, sortOption, pageable);

        return widgetPageHelper.getPagedResponse(widgetPage, UpWidgetResDto::from);
    }

    @Transactional
    public UpWidgetResDto update(Long id, UpWidgetCreateDto widgetCreateDto) {
        Widget widget = widgetRepository.findById(id).orElseThrow(() -> new WidgetNotFoundException(id, WidgetType.UP));

        widget.updateBaseField(widgetCreateDto.getName(), widgetCreateDto.getUrl());
        widget.updateProperties(
                new UpWidgetProperties(widgetCreateDto.getPeriodStart(), widgetCreateDto.getPeriodEnd()));

        eventPublisher.publishEvent(new HomeCacheEvictEvent(widget.getFestival().getId()));
        return UpWidgetResDto.from(widget);
    }

    @Transactional
    public void delete(Long upWidgetId) {
        Widget widget = widgetRepository
                .findById(upWidgetId)
                .orElseThrow(() -> new WidgetNotFoundException(upWidgetId, WidgetType.UP));
        Long festivalId = widget.getFestival().getId();
        widgetRepository.delete(widget);
        eventPublisher.publishEvent(new HomeCacheEvictEvent(festivalId));
    }

    @Transactional(readOnly = true)
    public List<UpWidgetResDto> getUpWidgetsByNow(Long festivalId, LocalDateTime now) {
        return widgetRepository.findUpWidgetsByFestivalIdAndPeriod(festivalId, now).stream()
                .map(UpWidgetResDto::from)
                .collect(Collectors.toList());
    }

    private void validateFestival(Long festivalId) {
        if (!festivalRepository.existsById(festivalId)) {
            throw new FestivalNotFoundException(festivalId);
        }
    }
}
