package com.halo.eventer.domain.widget.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.widget.WidgetType;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetResDto;
import com.halo.eventer.domain.widget.entity.DownWidget;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.repository.DownWidgetRepository;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.global.util.DisplayOrderUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DownWidgetService {

    private final DownWidgetRepository downWidgetRepository;
    private final FestivalRepository festivalRepository;

    @Transactional
    public DownWidgetResDto create(Long festivalId, DownWidgetCreateDto downWidgetCreateDto) {
        Festival festival =
                festivalRepository.findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));

        DownWidget downWidget = downWidgetRepository.save(DownWidget.from(festival, downWidgetCreateDto));

        return DownWidgetResDto.from(downWidget);
    }

    @Transactional(readOnly = true)
    public List<DownWidgetResDto> getAllDownWidgets(Long festivalId) {
        return downWidgetRepository.findAllByFestivalId(festivalId).stream()
                .map(DownWidgetResDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public DownWidgetResDto update(Long id, DownWidgetCreateDto downWidgetCreateDto) {
        DownWidget downWidget =
                downWidgetRepository.findById(id).orElseThrow(() -> new WidgetNotFoundException(id, WidgetType.DOWN));
        downWidget.updateDownWidget(downWidgetCreateDto);
        return DownWidgetResDto.from(downWidget);
    }

    @Transactional
    public void delete(Long id) {
        downWidgetRepository.deleteById(id);
    }

    @Transactional
    public List<DownWidgetResDto> updateDisplayOrder(Long festivalId, List<OrderUpdateRequest> orderRequests) {
        List<DownWidget> widgets = downWidgetRepository.findAllByFestivalId(festivalId);

        DisplayOrderUtils.updateDisplayOrder(widgets, orderRequests);

        return widgets.stream().map(DownWidgetResDto::from).collect(Collectors.toList());
    }
}
