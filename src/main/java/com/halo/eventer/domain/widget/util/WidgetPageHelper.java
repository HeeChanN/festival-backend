package com.halo.eventer.domain.widget.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.WidgetType;
import com.halo.eventer.domain.widget.exception.SortOptionNotFoundException;
import com.halo.eventer.domain.widget.repository.WidgetRepository;
import com.halo.eventer.global.common.page.PageInfo;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.sort.SortOption;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WidgetPageHelper {

    private final WidgetRepository widgetRepository;

    public Page<Widget> findWidgetsBySort(
            WidgetType widgetType, Long festivalId, SortOption sortOption, Pageable pageable) {
        switch (sortOption) {
            case CREATED_AT:
                return widgetRepository.findByFestivalIdAndWidgetTypeOrderByCreatedAtDesc(
                        festivalId, widgetType, pageable);

            case UPDATED_AT:
                return widgetRepository.findByFestivalIdAndWidgetTypeOrderByUpdatedAtDesc(
                        festivalId, widgetType, pageable);

            default:
                throw new SortOptionNotFoundException("Unsupported sortOption: " + sortOption);
        }
    }

    public <D> PagedResponse<D> getPagedResponse(Page<Widget> page, Function<Widget, D> toDto) {
        List<D> dtoList = page.getContent().stream().map(toDto).collect(Collectors.toList());

        PageInfo pageInfo = PageInfo.builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return PagedResponse.<D>builder().content(dtoList).pageInfo(pageInfo).build();
    }
}
