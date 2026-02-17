package com.halo.eventer.domain.widget.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.WidgetFixture;
import com.halo.eventer.domain.widget.WidgetType;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetResDto;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.properties.MiddleWidgetProperties;
import com.halo.eventer.domain.widget.repository.WidgetRepository;
import com.halo.eventer.domain.widget.util.WidgetPageHelper;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.global.common.page.PageInfo;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.sort.SortOption;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class MiddleWidgetServiceTest {
    @Mock
    private FestivalRepository festivalRepository;

    @Mock
    private WidgetRepository widgetRepository;

    @Mock
    private WidgetPageHelper widgetPageHelper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private MiddleWidgetService middleWidgetService;

    private Festival festival;
    private Widget middleWidget;
    private MiddleWidgetCreateDto middleWidgetCreateDto;
    final long middleWidgetId = 1L;

    @BeforeEach
    void setUp() {
        festival = FestivalFixture.축제_엔티티();
        middleWidgetCreateDto = WidgetFixture.중간_위젯_생성_DTO();
        middleWidget = WidgetFixture.중간_위젯_엔티티(festival, middleWidgetCreateDto);
        setField(middleWidget, "id", middleWidgetId);
    }

    @Test
    void 중간위젯_생성_테스트() {
        final long festivalId = 1L;
        given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
        given(widgetRepository.save(any(Widget.class))).willReturn(middleWidget);

        MiddleWidgetResDto result = middleWidgetService.create(festivalId, middleWidgetCreateDto);

        assertResultDtoEqualsWidget(result, middleWidget);
    }

    @Test
    void 중간위젯_생성_축제_없을때_예외() {
        final long festivalId = 1L;
        given(festivalRepository.findById(festivalId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> middleWidgetService.create(festivalId, middleWidgetCreateDto))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 중간위젯_id_단일조회() {
        given(widgetRepository.findById(middleWidgetId)).willReturn(Optional.of(middleWidget));

        MiddleWidgetResDto result = middleWidgetService.getMiddleWidget(middleWidgetId);

        assertResultDtoEqualsWidget(result, middleWidget);
    }

    @Test
    void 중간위젯_id_단일조회시_없을때_예외() {
        given(widgetRepository.findById(middleWidgetId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> middleWidgetService.getMiddleWidget(middleWidgetId))
                .isInstanceOf(WidgetNotFoundException.class);
    }

    @Test
    void 중간위젯_offset_페이징_조회_테스트() {
        final long festivalId = 1L;
        final int page = 0, size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<Widget> widgetPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        PageInfo pageInfo = PageInfo.builder()
                .pageNumber(widgetPage.getNumber())
                .pageSize(widgetPage.getSize())
                .totalElements(widgetPage.getTotalElements())
                .totalPages(widgetPage.getTotalPages())
                .build();
        PagedResponse<MiddleWidgetResDto> expected = new PagedResponse<>(Collections.emptyList(), pageInfo);
        given(widgetPageHelper.findWidgetsBySort(WidgetType.MIDDLE, festivalId, SortOption.CREATED_AT, pageable))
                .willReturn(widgetPage);
        given(festivalRepository.existsById(festivalId)).willReturn(true);
        given(widgetPageHelper.getPagedResponse(any(), any(Function.class))).willReturn(expected);

        PagedResponse<MiddleWidgetResDto> result =
                middleWidgetService.getMiddleWidgetsWithOffsetPaging(festivalId, SortOption.CREATED_AT, page, size);

        assertThat(result).isSameAs(expected);
    }

    @Test
    void 중간위젯_수정_테스트() {
        given(widgetRepository.findById(middleWidgetId)).willReturn(Optional.of(middleWidget));

        MiddleWidgetResDto result = middleWidgetService.update(middleWidgetId, middleWidgetCreateDto);

        assertResultDtoEqualsWidget(result, middleWidget);
    }

    @Test
    void 중간위젯_수정할때_조회되지_않는경우_예외() {
        given(widgetRepository.findById(middleWidgetId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> middleWidgetService.update(middleWidgetId, middleWidgetCreateDto))
                .isInstanceOf(WidgetNotFoundException.class);
    }

    @Test
    void 중간위젯_삭제_테스트() {
        given(widgetRepository.findById(middleWidgetId)).willReturn(Optional.of(middleWidget));

        middleWidgetService.delete(middleWidgetId);

        then(widgetRepository).should().delete(middleWidget);
    }

    @Test
    void 중간위젯_순서_수정_테스트() {
        final long festivalId = 1L;
        final int displayOrder = 1;
        given(widgetRepository.findAllByFestivalIdAndWidgetType(festivalId, WidgetType.MIDDLE))
                .willReturn(List.of(middleWidget));
        List<OrderUpdateRequest> request = List.of(OrderUpdateRequest.of(middleWidgetId, displayOrder));

        List<MiddleWidgetResDto> result = middleWidgetService.updateDisplayOrder(festivalId, request);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDisplayOrder()).isEqualTo(displayOrder);
    }

    private void assertResultDtoEqualsWidget(MiddleWidgetResDto result, Widget widget) {
        MiddleWidgetProperties props = widget.getTypedProperties(MiddleWidgetProperties.class);
        assertThat(result.getName()).isEqualTo(widget.getName());
        assertThat(result.getUrl()).isEqualTo(widget.getUrl());
        assertThat(result.getImage()).isEqualTo(props.getImage());
        assertThat(result.getDisplayOrder()).isEqualTo(widget.getDisplayOrder());
    }
}
