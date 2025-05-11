package com.halo.eventer.domain.widget.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.halo.eventer.domain.widget.BaseWidget;
import com.halo.eventer.domain.widget.exception.SortOptionNotFoundException;
import com.halo.eventer.domain.widget.repository.BaseWidgetRepository;
import com.halo.eventer.global.common.page.PageInfo;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.sort.SortOption;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WidgetPageHelper {

    private final BaseWidgetRepository baseWidgetRepository;

    public <E extends BaseWidget> Page<E> findWidgetsBySort(
            Class<E> childClass, Long festivalId, SortOption sortOption, Pageable pageable) {
        switch (sortOption) {
            case CREATED_AT:
                return baseWidgetRepository.findChildCreateDesc(childClass, festivalId, pageable);

            case UPDATED_AT:
                return baseWidgetRepository.findChildUpdateDesc(childClass, festivalId, pageable);

            default:
                throw new SortOptionNotFoundException("Unsupported sortOption: " + sortOption);
        }
    }

    public <E extends BaseWidget, D> PagedResponse<D> getPagedResponse(Page<E> page, Function<E, D> toDto) {
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
