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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.widget.WidgetFixture;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetResDto;
import com.halo.eventer.domain.widget.entity.SquareWidget;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.repository.SquareWidgetRepository;
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
    private SquareWidgetRepository squareWidgetRepository;

    @Mock
    private WidgetPageHelper widgetPageHelper;

    @InjectMocks
    private SquareWidgetService squareWidgetService;

    private Festival festival;
    private SquareWidget squareWidget;
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
        // given
        final long festivalId = 1L;
        given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
        given(squareWidgetRepository.save(any(SquareWidget.class))).willReturn(squareWidget);

        // when
        SquareWidgetResDto result = squareWidgetService.create(festivalId, squareWidgetCreateDto);

        // then
        assertResultDtoEqualsSquareWidget(result, squareWidget);
    }

    @Test
    void 정사각형위젯_생성_축제_없을때_예외() {
        // given
        final long festivalId = 1L;
        given(festivalRepository.findById(festivalId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> squareWidgetService.create(festivalId, squareWidgetCreateDto))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 정사각형위젯_id_단일조회() {
        // given
        given(squareWidgetRepository.findById(squareWidgetId)).willReturn(Optional.of(squareWidget));

        // when
        SquareWidgetResDto result = squareWidgetService.getSquareWidget(squareWidgetId);

        // then
        assertResultDtoEqualsSquareWidget(result, squareWidget);
    }

    @Test
    void 정사각형위젯_id_단일조회시_없을때_예외() {
        // given
        given(squareWidgetRepository.findById(squareWidgetId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> squareWidgetService.getSquareWidget(squareWidgetId))
                .isInstanceOf(WidgetNotFoundException.class);
    }

    @Test
    void 정사각형위젯_offset_페이징_조회_테스트() {
        // given
        final long festivalId = 1L;
        final int page = 0, size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<SquareWidget> widgetPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        PageInfo pageInfo = PageInfo.builder()
                .pageNumber(widgetPage.getNumber())
                .pageSize(widgetPage.getSize())
                .totalElements(widgetPage.getTotalElements())
                .totalPages(widgetPage.getTotalPages())
                .build();
        PagedResponse<SquareWidgetResDto> expected = new PagedResponse<>(Collections.emptyList(), pageInfo);
        given(widgetPageHelper.findWidgetsBySort(SquareWidget.class, festivalId, SortOption.CREATED_AT, pageable))
                .willReturn(widgetPage);
        given(festivalRepository.existsById(festivalId)).willReturn(true);
        given(widgetPageHelper.getPagedResponse(any(), any(Function.class))).willReturn(expected);

        // when
        PagedResponse<SquareWidgetResDto> result =
                squareWidgetService.getSquareWidgetsWithOffsetPaging(festivalId, SortOption.CREATED_AT, page, size);

        // then
        assertThat(result).isSameAs(expected);
    }

    @Test
    void 정사각형위젯_수정_테스트() {
        // given
        given(squareWidgetRepository.findById(squareWidgetId)).willReturn(Optional.of(squareWidget));

        // when
        SquareWidgetResDto result = squareWidgetService.update(squareWidgetId, squareWidgetCreateDto);

        // then
        assertResultDtoEqualsSquareWidget(result, squareWidget);
    }

    @Test
    void 정사각형위젯_수정할때_조회되지_않는경우_예외() {
        // given
        given(squareWidgetRepository.findById(squareWidgetId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> squareWidgetService.update(squareWidgetId, squareWidgetCreateDto))
                .isInstanceOf(WidgetNotFoundException.class);
    }

    @Test
    void 정사각형위젯_삭제_테스트() {
        // when
        squareWidgetService.delete(squareWidgetId);

        // then
        then(squareWidgetRepository).should().deleteById(squareWidgetId);
    }

    @Test
    void 정사각형위젯_순서_수정_테스트() {
        // given
        final long festivalId = 1L;
        final int displayOrder = 1;
        given(squareWidgetRepository.findAllByFestivalId(festivalId)).willReturn(List.of(squareWidget));
        List<OrderUpdateRequest> request = List.of(OrderUpdateRequest.of(squareWidgetId, displayOrder));

        // when
        List<SquareWidgetResDto> result = squareWidgetService.updateDisplayOrder(squareWidgetId, request);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDisplayOrder()).isEqualTo(displayOrder);
    }

    private void assertResultDtoEqualsSquareWidget(SquareWidgetResDto result, SquareWidget squareWidget) {
        assertThat(result.getName()).isEqualTo(squareWidget.getName());
        assertThat(result.getUrl()).isEqualTo(squareWidget.getUrl());
        assertThat(result.getDescription())
                .isEqualTo(squareWidget.getDescriptionFeature().getDescription());
        assertThat(result.getIcon()).isEqualTo(squareWidget.getImageFeature().getImage());
        assertThat(result.getDisplayOrder())
                .isEqualTo(squareWidget.getDisplayOrderFeature().getDisplayOrder());
    }
}
