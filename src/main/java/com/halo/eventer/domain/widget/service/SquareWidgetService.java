package com.halo.eventer.domain.widget.service;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetResDto;
import com.halo.eventer.domain.widget.entity.SquareWidget;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.repository.SquareWidgetRepository;
import com.halo.eventer.global.util.DisplayOrderUtils;
import com.halo.eventer.domain.widget.util.WidgetPageHelper;

import com.halo.eventer.global.common.sort.SortOption;
import com.halo.eventer.domain.widget.WidgetType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SquareWidgetService {

  private final SquareWidgetRepository squareWidgetRepository;
  private final FestivalRepository festivalRepository;
  private final WidgetPageHelper widgetPageHelper;

  @Transactional
  public SquareWidgetResDto create(Long festivalId, SquareWidgetCreateDto squareWidgetCreateDto) {
    Festival festival = festivalRepository.findById(festivalId)
            .orElseThrow(() -> new FestivalNotFoundException(festivalId));

    SquareWidget squareWidget = squareWidgetRepository.save(SquareWidget.from(festival, squareWidgetCreateDto));
    return SquareWidgetResDto.from(squareWidget);
  }

  @Transactional(readOnly = true)
  public SquareWidgetResDto getSquareWidget(Long id) {
    SquareWidget squareWidget = squareWidgetRepository.findById(id)
            .orElseThrow(() -> new WidgetNotFoundException(id, WidgetType.SQUARE));

    return SquareWidgetResDto.from(squareWidget);
  }

  @Transactional(readOnly = true)
  public PagedResponse<SquareWidgetResDto> getSquareWidgetsWithOffsetPaging(Long festivalId, SortOption sortOption,
                                                                        int page, int size) {
      validateFestival(festivalId);

      Pageable pageable = PageRequest.of(page, size);
      Page<SquareWidget> squareWidgetPage = widgetPageHelper.
              findWidgetsBySort(SquareWidget.class,festivalId,sortOption,pageable);

      return widgetPageHelper.getPagedResponse(squareWidgetPage,SquareWidgetResDto::from);
  }

  @Transactional
  public SquareWidgetResDto update(Long id, SquareWidgetCreateDto squareWidgetCreateDto) {
    SquareWidget squareWidget = squareWidgetRepository.findById(id)
            .orElseThrow(() -> new WidgetNotFoundException(id,WidgetType.SQUARE));
    squareWidget.updateSquareWidget(squareWidgetCreateDto);
    return SquareWidgetResDto.from(squareWidget);
  }

  @Transactional
  public void delete(Long id) {
    squareWidgetRepository.deleteById(id);
  }

  @Transactional
  public List<SquareWidgetResDto> updateDisplayOrder(Long festivalId, List<OrderUpdateRequest> orderRequests){
      List<SquareWidget> widgets = squareWidgetRepository.findAllByFestivalId(festivalId);

      DisplayOrderUtils.updateDisplayOrder(widgets, orderRequests);

      return widgets.stream()
              .map(SquareWidgetResDto::from)
              .collect(Collectors.toList());
  }

  private void validateFestival(Long festivalId) {
    if (!festivalRepository.existsById(festivalId)) {
      throw new FestivalNotFoundException(festivalId);
    }
  }
}
