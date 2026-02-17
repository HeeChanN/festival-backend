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
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetResDto;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.properties.SquareWidgetProperties;
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
public class SquareWidgetServiceTest {

    @Mock
    private FestivalRepository festivalRepository;

    @Mock
    private WidgetRepository widgetRepository;

    @Mock
    private WidgetPageHelper widgetPageHelper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private SquareWidgetService squareWidgetService;

    private Festival festival;
    private Widget squareWidget;
    private SquareWidgetCreateDto squareWidgetCreateDto;
    final long squareWidgetId = 1L;

    @BeforeEach
    void setUp() {
        festival = FestivalFixture.축제_엔티티();
        squareWidgetCreateDto = WidgetFixture.정사각형_위젯_생성_DTO();
        squareWidget = WidgetFixture.정사각형_위젯_엔티티(festival, squareWidgetCreateDto);
        setField(squareWidget, "id", squareWidgetId);
    }

    @Test
    void 정사각형위젯_생성_테스트() {
        final long festivalId = 1L;
        given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
        given(widgetRepository.save(any(Widget.class))).willReturn(squareWidget);

        SquareWidgetResDto result = squareWidgetService.create(festivalId, squareWidgetCreateDto);

        assertResultDtoEqualsWidget(result, squareWidget);
    }

    @Test
    void 정사각형위젯_생성_축제_없을때_예외() {
        final long festivalId = 1L;
        given(festivalRepository.findById(festivalId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> squareWidgetService.create(festivalId, squareWidgetCreateDto))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 정사각형위젯_id_단일조회() {
        given(widgetRepository.findById(squareWidgetId)).willReturn(Optional.of(squareWidget));

        SquareWidgetResDto result = squareWidgetService.getSquareWidget(squareWidgetId);

        assertResultDtoEqualsWidget(result, squareWidget);
    }

    @Test
    void 정사각형위젯_id_단일조회시_없을때_예외() {
        given(widgetRepository.findById(squareWidgetId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> squareWidgetService.getSquareWidget(squareWidgetId))
                .isInstanceOf(WidgetNotFoundException.class);
    }

    @Test
    void 정사각형위젯_offset_페이징_조회_테스트() {
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
        PagedResponse<SquareWidgetResDto> expected = new PagedResponse<>(Collections.emptyList(), pageInfo);
        given(widgetPageHelper.findWidgetsBySort(WidgetType.SQUARE, festivalId, SortOption.CREATED_AT, pageable))
                .willReturn(widgetPage);
        given(festivalRepository.existsById(festivalId)).willReturn(true);
        given(widgetPageHelper.getPagedResponse(any(), any(Function.class))).willReturn(expected);

        PagedResponse<SquareWidgetResDto> result =
                squareWidgetService.getSquareWidgetsWithOffsetPaging(festivalId, SortOption.CREATED_AT, page, size);

        assertThat(result).isSameAs(expected);
    }

    @Test
    void 정사각형위젯_수정_테스트() {
        given(widgetRepository.findById(squareWidgetId)).willReturn(Optional.of(squareWidget));

        SquareWidgetResDto result = squareWidgetService.update(squareWidgetId, squareWidgetCreateDto);

        assertResultDtoEqualsWidget(result, squareWidget);
    }

    @Test
    void 정사각형위젯_수정할때_조회되지_않는경우_예외() {
        given(widgetRepository.findById(squareWidgetId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> squareWidgetService.update(squareWidgetId, squareWidgetCreateDto))
                .isInstanceOf(WidgetNotFoundException.class);
    }

    @Test
    void 정사각형위젯_삭제_테스트() {
        given(widgetRepository.findById(squareWidgetId)).willReturn(Optional.of(squareWidget));

        squareWidgetService.delete(squareWidgetId);

        then(widgetRepository).should().delete(squareWidget);
    }

    @Test
    void 정사각형위젯_순서_수정_테스트() {
        final long festivalId = 1L;
        final int displayOrder = 1;
        given(widgetRepository.findAllByFestivalIdAndWidgetType(festivalId, WidgetType.SQUARE))
                .willReturn(List.of(squareWidget));
        List<OrderUpdateRequest> request = List.of(OrderUpdateRequest.of(squareWidgetId, displayOrder));

        List<SquareWidgetResDto> result = squareWidgetService.updateDisplayOrder(festivalId, request);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDisplayOrder()).isEqualTo(displayOrder);
    }

    private void assertResultDtoEqualsWidget(SquareWidgetResDto result, Widget widget) {
        SquareWidgetProperties props = widget.getTypedProperties(SquareWidgetProperties.class);
        assertThat(result.getName()).isEqualTo(widget.getName());
        assertThat(result.getUrl()).isEqualTo(widget.getUrl());
        assertThat(result.getDescription()).isEqualTo(props.getDescription());
        assertThat(result.getIcon()).isEqualTo(props.getImage());
        assertThat(result.getDisplayOrder()).isEqualTo(widget.getDisplayOrder());
    }
}
