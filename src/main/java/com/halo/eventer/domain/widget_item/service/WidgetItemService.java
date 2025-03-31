package com.halo.eventer.domain.widget_item.service;

import com.halo.eventer.domain.image.dto.FileDto;
import com.halo.eventer.domain.image.dto.ImageDto;
import com.halo.eventer.domain.widget.BaseWidget;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.repository.BaseWidgetRepository;
import com.halo.eventer.domain.widget_item.WidgetItem;
import com.halo.eventer.domain.widget_item.dto.WidgetItemResDto;
import com.halo.eventer.domain.widget_item.dto.WidgetItemCreateDto;
import com.halo.eventer.domain.widget_item.exception.WidgetItemNotFoundException;
import com.halo.eventer.domain.widget_item.repository.WidgetItemRepository;
import com.halo.eventer.domain.image.ImageRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.halo.eventer.domain.widget.WidgetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WidgetItemService {

  private final WidgetItemRepository widgetItemRepository;
  private final ImageRepository imageRepository;
  private final BaseWidgetRepository baseWidgetRepository;

  @Transactional
  public WidgetItemResDto create(Long widgetId, WidgetItemCreateDto widgetItemCreateDto) {
    BaseWidget baseWidget = baseWidgetRepository.findById(widgetId)
            .orElseThrow(() -> new WidgetNotFoundException(widgetId, WidgetType.DEFAULT));

    WidgetItem widgetItem = widgetItemRepository.save(WidgetItem.from(baseWidget,widgetItemCreateDto));
    return WidgetItemResDto.from(widgetItem);
  }

  @Transactional(readOnly = true)
  public List<WidgetItemResDto> getWidgetItemWithWidgetId(Long widgetId) {
    List<WidgetItem> widgetItems = widgetItemRepository.findAllWidgetItemsByBaseWidgetId(widgetId);

    return widgetItems.stream()
            .map(WidgetItemResDto::from)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<ImageDto> getWidgetItemImages(Long id) {
    WidgetItem widgetItem = widgetItemRepository.findWidgetItemById(id)
            .orElseThrow(()-> new WidgetItemNotFoundException(id));

    return widgetItem.getImages().stream()
            .map(ImageDto::from)
            .collect(Collectors.toList());
  }

  @Transactional
  public WidgetItemResDto updateWidgetInfo(Long id, WidgetItemCreateDto widgetItemCreateDto) {
    WidgetItem widgetItem = widgetItemRepository.findById(id)
                    .orElseThrow(()-> new WidgetItemNotFoundException(id));
    widgetItem.updateWidgetItem(widgetItemCreateDto);
    return WidgetItemResDto.from(widgetItem);
  }

  @Transactional
  public List<ImageDto> addImages(Long id,List<FileDto> fileDtos){
    WidgetItem widgetItem = widgetItemRepository.findById(id)
            .orElseThrow(()-> new WidgetItemNotFoundException(id));

    fileDtos.forEach(o->widgetItem.addImages(o.getUrl()));
    return widgetItem.getImages().stream()
            .map(ImageDto::from)
            .collect(Collectors.toList());
  }

  @Transactional
  public void deleteImages(Long id, List<Long> imageIds){
    Set<Long> imageMap = new HashSet<>(imageIds);

    widgetItemRepository.findWidgetItemById(id).orElseThrow(() -> new WidgetItemNotFoundException(id))
        .getImages()
        .removeIf(image -> imageMap.contains(image.getId()));
  }

  @Transactional
  public void delete(Long id) {
    widgetItemRepository.deleteById(id);
  }
}
