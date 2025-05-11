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
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetResDto;
import com.halo.eventer.domain.widget.entity.MiddleWidget;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.repository.MiddleWidgetRepository;
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
    private MiddleWidgetRepository middleWidgetRepository;

    @Mock
    private WidgetPageHelper widgetPageHelper;

    @InjectMocks
    private MiddleWidgetService middleWidgetService;

    private Festival festival;
    private MiddleWidget middleWidget;
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
        // given
        final long festivalId = 1L;
        given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
        given(middleWidgetRepository.save(any(MiddleWidget.class))).willReturn(middleWidget);

        // when
        MiddleWidgetResDto result = middleWidgetService.create(festivalId, middleWidgetCreateDto);

        // then
        assertResultDtoEqualsMiddleWidget(result, middleWidget);
    }

    @Test
    void 중간위젯_생성_축제_없을때_예외() {
        // given
        final long festivalId = 1L;
        given(festivalRepository.findById(festivalId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> middleWidgetService.create(festivalId, middleWidgetCreateDto))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 중간위젯_id_단일조회() {
        // given
        given(middleWidgetRepository.findById(middleWidgetId)).willReturn(Optional.of(middleWidget));

        // when
        MiddleWidgetResDto result = middleWidgetService.getMiddleWidget(middleWidgetId);

        // then
        assertResultDtoEqualsMiddleWidget(result, middleWidget);
    }

    @Test
    void 중간위젯_id_단일조회시_없을때_예외() {
        // given
        given(middleWidgetRepository.findById(middleWidgetId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> middleWidgetService.getMiddleWidget(middleWidgetId))
                .isInstanceOf(WidgetNotFoundException.class);
    }

    @Test
    void 중간위젯_offset_페이징_조회_테스트() {
        // given
        final long festivalId = 1L;
        final int page = 0, size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<MiddleWidget> widgetPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        PageInfo pageInfo = PageInfo.builder()
                .pageNumber(widgetPage.getNumber())
                .pageSize(widgetPage.getSize())
                .totalElements(widgetPage.getTotalElements())
                .totalPages(widgetPage.getTotalPages())
                .build();
        PagedResponse<MiddleWidgetResDto> expected = new PagedResponse<>(Collections.emptyList(), pageInfo);
        given(widgetPageHelper.findWidgetsBySort(MiddleWidget.class, festivalId, SortOption.CREATED_AT, pageable))
                .willReturn(widgetPage);
        given(festivalRepository.existsById(festivalId)).willReturn(true);
        given(widgetPageHelper.getPagedResponse(any(), any(Function.class))).willReturn(expected);

        // when
        PagedResponse<MiddleWidgetResDto> result =
                middleWidgetService.getMiddleWidgetsWithOffsetPaging(festivalId, SortOption.CREATED_AT, page, size);

        // then
        assertThat(result).isSameAs(expected);
    }

    @Test
    void 중간위젯_수정_테스트() {
        // given
        given(middleWidgetRepository.findById(middleWidgetId)).willReturn(Optional.of(middleWidget));

        // when
        MiddleWidgetResDto result = middleWidgetService.update(middleWidgetId, middleWidgetCreateDto);

        // then
        assertResultDtoEqualsMiddleWidget(result, middleWidget);
    }

    @Test
    void 중간위젯_수정할때_조회되지_않는경우_예외() {
        // given
        given(middleWidgetRepository.findById(middleWidgetId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> middleWidgetService.update(middleWidgetId, middleWidgetCreateDto))
                .isInstanceOf(WidgetNotFoundException.class);
    }

    @Test
    void 중간위젯_삭제_테스트() {
        // when
        middleWidgetService.delete(middleWidgetId);

        // then
        then(middleWidgetRepository).should().deleteById(middleWidgetId);
    }

    @Test
    void 중간위젯_순서_수정_테스트() {
        // given
        final long festivalId = 1L;
        final int displayOrder = 1;
        given(middleWidgetRepository.findAllByFestivalId(festivalId)).willReturn(List.of(middleWidget));
        List<OrderUpdateRequest> request = List.of(OrderUpdateRequest.of(middleWidgetId, displayOrder));

        // when
        List<MiddleWidgetResDto> result = middleWidgetService.updateDisplayOrder(middleWidgetId, request);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDisplayOrder()).isEqualTo(displayOrder);
    }

    private void assertResultDtoEqualsMiddleWidget(MiddleWidgetResDto result, MiddleWidget middleWidget) {
        assertThat(result.getName()).isEqualTo(middleWidget.getName());
        assertThat(result.getUrl()).isEqualTo(middleWidget.getUrl());
        assertThat(result.getDisplayOrder())
                .isEqualTo(middleWidget.getDisplayOrderFeature().getDisplayOrder());
    }
}
