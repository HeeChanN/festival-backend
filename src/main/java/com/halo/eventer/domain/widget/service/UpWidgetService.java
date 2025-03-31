package com.halo.eventer.domain.widget.service;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetResDto;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.repository.UpWidgetRepository;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetCreateDto;
import com.halo.eventer.domain.widget.entity.UpWidget;

import com.halo.eventer.domain.widget.util.WidgetPageHelper;
import com.halo.eventer.global.common.sort.SortOption;
import com.halo.eventer.domain.widget.WidgetType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UpWidgetService {

  private final UpWidgetRepository upWidgetRepository;
  private final FestivalRepository festivalRepository;
  private final WidgetPageHelper widgetPageHelper;

  @Transactional
  public UpWidgetResDto create(Long festivalId, UpWidgetCreateDto upWidgetCreateDto) {
    Festival festival = festivalRepository
            .findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));

    UpWidget upWidget = upWidgetRepository.save(UpWidget.from(festival,upWidgetCreateDto));
    return UpWidgetResDto.from(upWidget);
  }

  @Transactional(readOnly = true)
  public UpWidgetResDto getUpWidget(Long id) {
    UpWidget upWidget = upWidgetRepository.findById(id).orElseThrow(() -> new WidgetNotFoundException(id, WidgetType.UP));

    return UpWidgetResDto.from(upWidget);
  }

  @Transactional(readOnly = true)
  public PagedResponse<UpWidgetResDto> getUpWidgetsWithOffsetPaging(Long festivalId, SortOption sortOption,
                                                                    int page, int size) {
    validateFestival(festivalId);

    Pageable pageable = PageRequest.of(page, size);
    Page<UpWidget> upWidgetPage = widgetPageHelper.findWidgetsBySort(UpWidget.class,festivalId,sortOption,pageable);

    return widgetPageHelper.getPagedResponse(upWidgetPage, UpWidgetResDto::from);
  }

  @Transactional
  public UpWidgetResDto update(Long id, UpWidgetCreateDto widgetCreateDto) {
    UpWidget upWidget = upWidgetRepository.findById(id).orElseThrow(() -> new WidgetNotFoundException(id,WidgetType.UP));

    upWidget.updateUpWidget(widgetCreateDto);

    return UpWidgetResDto.from(upWidget);
  }

  @Transactional
  public void delete(Long upWidgetId) {
    upWidgetRepository.deleteById(upWidgetId);
  }

  @Transactional(readOnly = true)
  public List<UpWidgetResDto> getUpWidgetsByNow(Long festivalId, LocalDateTime now) {
    return upWidgetRepository.findAllByFestivalIdAndPeriod(festivalId, now).stream()
            .map(UpWidgetResDto::from)
            .collect(Collectors.toList());
  }

  private void validateFestival(Long festivalId){
    if (!festivalRepository.existsById(festivalId)) {
      throw new FestivalNotFoundException(festivalId);
    }
  }
}
