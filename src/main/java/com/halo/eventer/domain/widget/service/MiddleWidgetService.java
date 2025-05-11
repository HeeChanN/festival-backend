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
import com.halo.eventer.domain.widget.WidgetType;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetResDto;
import com.halo.eventer.domain.widget.entity.MiddleWidget;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.repository.MiddleWidgetRepository;
import com.halo.eventer.domain.widget.util.WidgetPageHelper;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.sort.SortOption;
import com.halo.eventer.global.util.DisplayOrderUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MiddleWidgetService {

    private final MiddleWidgetRepository middleWidgetRepository;
    private final FestivalRepository festivalRepository;
    private final WidgetPageHelper widgetPageHelper;

    @Transactional
    public MiddleWidgetResDto create(Long festivalId, MiddleWidgetCreateDto middleWidgetCreateDto) {
        Festival festival =
                festivalRepository.findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));

        MiddleWidget middleWidget = middleWidgetRepository.save(MiddleWidget.from(festival, middleWidgetCreateDto));

        return MiddleWidgetResDto.from(middleWidget);
    }

    @Transactional(readOnly = true)
    public MiddleWidgetResDto getMiddleWidget(Long id) {
        MiddleWidget middleWidget = middleWidgetRepository
                .findById(id)
                .orElseThrow(() -> new WidgetNotFoundException(id, WidgetType.MIDDLE));

        return MiddleWidgetResDto.from(middleWidget);
    }

    @Transactional(readOnly = true)
    public PagedResponse<MiddleWidgetResDto> getMiddleWidgetsWithOffsetPaging(
            Long festivalId, SortOption sortOption, int page, int size) {
        validateFestival(festivalId);

        Pageable pageable = PageRequest.of(page, size);
        Page<MiddleWidget> middleWidgetPage =
                widgetPageHelper.findWidgetsBySort(MiddleWidget.class, festivalId, sortOption, pageable);

        return widgetPageHelper.getPagedResponse(middleWidgetPage, MiddleWidgetResDto::from);
    }

    @Transactional
    public MiddleWidgetResDto update(Long id, MiddleWidgetCreateDto middleWidgetCreateDto) {
        MiddleWidget middleWidget = middleWidgetRepository
                .findById(id)
                .orElseThrow(() -> new WidgetNotFoundException(id, WidgetType.MIDDLE));

        middleWidget.updateMiddleWidget(middleWidgetCreateDto);

        return MiddleWidgetResDto.from(middleWidget);
    }

    @Transactional
    public void delete(Long id) {
        middleWidgetRepository.deleteById(id);
    }

    @Transactional
    public List<MiddleWidgetResDto> updateDisplayOrder(Long festivalId, List<OrderUpdateRequest> orderRequests) {
        List<MiddleWidget> widgets = middleWidgetRepository.findAllByFestivalId(festivalId);

        DisplayOrderUtils.updateDisplayOrder(widgets, orderRequests);

        return widgets.stream().map(MiddleWidgetResDto::from).collect(Collectors.toList());
    }

    private void validateFestival(Long festivalId) {
        if (!festivalRepository.existsById(festivalId)) {
            throw new FestivalNotFoundException(festivalId);
        }
    }
}
