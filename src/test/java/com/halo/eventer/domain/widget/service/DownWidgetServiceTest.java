package com.halo.eventer.domain.widget.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.WidgetFixture;
import com.halo.eventer.domain.widget.WidgetType;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetResDto;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.repository.WidgetRepository;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class DownWidgetServiceTest {

    @Mock
    private FestivalRepository festivalRepository;

    @Mock
    private WidgetRepository widgetRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private DownWidgetService downWidgetService;

    private Festival festival;
    private Widget downWidget;
    private DownWidgetCreateDto downWidgetCreateDto;
    final long downWidgetId = 1L;

    @BeforeEach
    void setUp() {
        festival = FestivalFixture.축제_엔티티();
        downWidgetCreateDto = WidgetFixture.하단_위젯_생성_DTO();
        downWidget = WidgetFixture.하단_위젯_엔티티(festival, downWidgetCreateDto);
        setField(downWidget, "id", downWidgetId);
    }

    @Test
    void 하단위젯_생성_테스트() {
        final long festivalId = 1L;
        given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
        given(widgetRepository.save(any(Widget.class))).willReturn(downWidget);

        DownWidgetResDto result = downWidgetService.create(festivalId, downWidgetCreateDto);

        assertResultDtoEqualsWidget(result, downWidget);
    }

    @Test
    void 하단위젯_생성_축제_없을때_예외() {
        final long festivalId = 1L;
        given(festivalRepository.findById(festivalId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> downWidgetService.create(festivalId, downWidgetCreateDto))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 하단위젯_리스트_조회() {
        final long festivalId = 1L;
        given(widgetRepository.findAllByFestivalIdAndWidgetType(festivalId, WidgetType.DOWN))
                .willReturn(List.of(downWidget));

        List<DownWidgetResDto> result = downWidgetService.getAllDownWidgets(festivalId);

        assertThat(result).hasSize(1);
        assertResultDtoEqualsWidget(result.get(0), downWidget);
    }

    @Test
    void 하단위젯_수정_테스트() {
        given(widgetRepository.findById(downWidgetId)).willReturn(Optional.of(downWidget));

        DownWidgetResDto result = downWidgetService.update(downWidgetId, downWidgetCreateDto);

        assertResultDtoEqualsWidget(result, downWidget);
    }

    @Test
    void 하단위젯_수정할때_없는경우_예외() {
        given(widgetRepository.findById(downWidgetId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> downWidgetService.update(downWidgetId, downWidgetCreateDto))
                .isInstanceOf(WidgetNotFoundException.class);
    }

    @Test
    void 하단위젯_삭제_테스트() {
        given(widgetRepository.findById(downWidgetId)).willReturn(Optional.of(downWidget));

        downWidgetService.delete(downWidgetId);

        then(widgetRepository).should().delete(downWidget);
    }

    @Test
    void 하단위젯_순서_수정_테스트() {
        final long festivalId = 1L;
        final int displayOrder = 1;
        given(widgetRepository.findAllByFestivalIdAndWidgetType(festivalId, WidgetType.DOWN))
                .willReturn(List.of(downWidget));
        List<OrderUpdateRequest> request = List.of(OrderUpdateRequest.of(downWidgetId, displayOrder));

        List<DownWidgetResDto> result = downWidgetService.updateDisplayOrder(festivalId, request);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDisplayOrder()).isEqualTo(displayOrder);
    }

    private void assertResultDtoEqualsWidget(DownWidgetResDto result, Widget widget) {
        assertThat(result.getName()).isEqualTo(widget.getName());
        assertThat(result.getUrl()).isEqualTo(widget.getUrl());
        assertThat(result.getId()).isEqualTo(widget.getId());
        assertThat(result.getDisplayOrder()).isEqualTo(widget.getDisplayOrder());
    }
}
