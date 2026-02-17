package com.halo.eventer.domain.widget.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.WidgetType;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetResDto;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.properties.MiddleWidgetProperties;
import com.halo.eventer.domain.widget.repository.WidgetRepository;
import com.halo.eventer.domain.widget.util.WidgetPageHelper;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.sort.SortOption;
import com.halo.eventer.global.util.DisplayOrderUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MiddleWidgetService {

    private final WidgetRepository widgetRepository;
    private final FestivalRepository festivalRepository;
    private final WidgetPageHelper widgetPageHelper;

    @Transactional
    public MiddleWidgetResDto create(Long festivalId, MiddleWidgetCreateDto middleWidgetCreateDto) {
        Festival festival =
                festivalRepository.findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));

        Widget widget = widgetRepository.save(Widget.createMiddleWidget(
                festival,
                middleWidgetCreateDto.getName(),
                middleWidgetCreateDto.getUrl(),
                middleWidgetCreateDto.getImage(),
                com.halo.eventer.global.constants.DisplayOrderConstants.DISPLAY_ORDER_DEFAULT));

        return MiddleWidgetResDto.from(widget);
    }

    @Transactional(readOnly = true)
    public MiddleWidgetResDto getMiddleWidget(Long id) {
        Widget widget =
                widgetRepository.findById(id).orElseThrow(() -> new WidgetNotFoundException(id, WidgetType.MIDDLE));

        return MiddleWidgetResDto.from(widget);
    }

    @Transactional(readOnly = true)
    public PagedResponse<MiddleWidgetResDto> getMiddleWidgetsWithOffsetPaging(
            Long festivalId, SortOption sortOption, int page, int size) {
        validateFestival(festivalId);

        Pageable pageable = PageRequest.of(page, size);
        Page<Widget> widgetPage =
                widgetPageHelper.findWidgetsBySort(WidgetType.MIDDLE, festivalId, sortOption, pageable);

        return widgetPageHelper.getPagedResponse(widgetPage, MiddleWidgetResDto::from);
    }

    @Transactional
    public MiddleWidgetResDto update(Long id, MiddleWidgetCreateDto middleWidgetCreateDto) {
        Widget widget =
                widgetRepository.findById(id).orElseThrow(() -> new WidgetNotFoundException(id, WidgetType.MIDDLE));

        widget.updateBaseField(middleWidgetCreateDto.getName(), middleWidgetCreateDto.getUrl());
        widget.updateProperties(new MiddleWidgetProperties(middleWidgetCreateDto.getImage()));

        return MiddleWidgetResDto.from(widget);
    }

    @Transactional
    public void delete(Long id) {
        widgetRepository.deleteById(id);
    }

    @Transactional
    public List<MiddleWidgetResDto> updateDisplayOrder(Long festivalId, List<OrderUpdateRequest> orderRequests) {
        List<Widget> widgets = widgetRepository.findAllByFestivalIdAndWidgetType(festivalId, WidgetType.MIDDLE);

        DisplayOrderUtils.updateDisplayOrder(widgets, orderRequests);

        return widgets.stream().map(MiddleWidgetResDto::from).collect(Collectors.toList());
    }

    private void validateFestival(Long festivalId) {
        if (!festivalRepository.existsById(festivalId)) {
            throw new FestivalNotFoundException(festivalId);
        }
    }
}
