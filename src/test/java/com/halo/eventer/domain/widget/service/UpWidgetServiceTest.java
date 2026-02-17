package com.halo.eventer.domain.widget.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.WidgetType;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetResDto;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.properties.UpWidgetProperties;
import com.halo.eventer.domain.widget.repository.WidgetRepository;
import com.halo.eventer.domain.widget.util.WidgetPageHelper;
import com.halo.eventer.global.common.page.PageInfo;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.sort.SortOption;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class UpWidgetServiceTest {

    @Mock
    private FestivalRepository festivalRepository;

    @Mock
    private WidgetRepository widgetRepository;

    @Mock
    private WidgetPageHelper widgetPageHelper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UpWidgetService upWidgetService;

    private Festival festival;
    private Widget upWidget;
    private UpWidgetCreateDto upWidgetCreateDto;
    private Pageable pageable;
    private List<Widget> upWidgetList;
    private Page<Widget> upWidgetPage;
    private PagedResponse<UpWidgetResDto> pagedResponse;
    private PageInfo pageInfo;

    private final long festivalId = 1L;
    private final long upWidgetId = 1L;

    @BeforeEach
    void setUp() {
        festival = new Festival();
        upWidget = Widget.createUpWidget(festival, "이름", "url", LocalDateTime.now(), LocalDateTime.now());
        upWidgetCreateDto = UpWidgetCreateDto.of("이름", "url", LocalDateTime.now(), LocalDateTime.now());
        pageable = PageRequest.of(0, 10);
        upWidgetList = Collections.singletonList(upWidget);
        upWidgetPage = new PageImpl<>(upWidgetList, pageable, upWidgetList.size());
        pageInfo = PageInfo.builder()
                .pageNumber(upWidgetPage.getNumber())
                .pageSize(upWidgetPage.getSize())
                .totalElements(upWidgetPage.getTotalElements())
                .totalPages(upWidgetPage.getTotalPages())
                .build();
        pagedResponse = new PagedResponse<>(
                upWidgetList.stream().map(UpWidgetResDto::from).collect(Collectors.toList()), pageInfo);
    }

    @Test
    void 상단위젯_생성() {
        given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
        given(widgetRepository.save(any())).willReturn(upWidget);

        UpWidgetResDto result = upWidgetService.create(festivalId, upWidgetCreateDto);

        UpWidgetProperties props = upWidget.getTypedProperties(UpWidgetProperties.class);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(upWidget.getName());
        assertThat(result.getUrl()).isEqualTo(upWidget.getUrl());
        assertThat(result.getPeriodStart()).isEqualTo(props.getPeriodStart());
        assertThat(result.getPeriodEnd()).isEqualTo(props.getPeriodEnd());
    }

    @Test
    void 상단위젯_생성_축제_없을때_예외() {
        given(festivalRepository.findById(festivalId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> upWidgetService.create(festivalId, upWidgetCreateDto))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 상단위젯_단일_조회() {
        given(widgetRepository.findById(upWidgetId)).willReturn(Optional.of(upWidget));

        UpWidgetResDto result = upWidgetService.getUpWidget(upWidgetId);

        UpWidgetProperties props = upWidget.getTypedProperties(UpWidgetProperties.class);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(upWidget.getName());
        assertThat(result.getUrl()).isEqualTo(upWidget.getUrl());
        assertThat(result.getPeriodStart()).isEqualTo(props.getPeriodStart());
        assertThat(result.getPeriodEnd()).isEqualTo(props.getPeriodEnd());
    }

    @Test
    void 상단위젯_없을때_예외() {
        given(widgetRepository.findById(upWidgetId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> upWidgetService.getUpWidget(upWidgetId)).isInstanceOf(WidgetNotFoundException.class);
    }

    @Test
    void 상단위젯_번호_페이징_조회() {
        given(widgetPageHelper.findWidgetsBySort(WidgetType.UP, festivalId, SortOption.CREATED_AT, pageable))
                .willReturn(upWidgetPage);
        given(festivalRepository.existsById(festivalId)).willReturn(true);
        given(widgetPageHelper.getPagedResponse(any(), any(Function.class))).willReturn(pagedResponse);

        PagedResponse<UpWidgetResDto> result =
                upWidgetService.getUpWidgetsWithOffsetPaging(festivalId, SortOption.CREATED_AT, 0, 10);
        UpWidgetResDto target = result.getContent().get(0);

        UpWidgetProperties props = upWidget.getTypedProperties(UpWidgetProperties.class);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(target.getName()).isEqualTo(upWidget.getName());
        assertThat(target.getUrl()).isEqualTo(upWidget.getUrl());
        assertThat(target.getPeriodStart()).isEqualTo(props.getPeriodStart());
        assertThat(target.getPeriodEnd()).isEqualTo(props.getPeriodEnd());
    }

    @Test
    void 상단위젯_번호_페이징_축제_없을때_예외() {
        given(festivalRepository.existsById(festivalId)).willReturn(false);

        assertThatThrownBy(() -> upWidgetService.getUpWidgetsWithOffsetPaging(festivalId, SortOption.CREATED_AT, 0, 10))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 상단위젯_수정() {
        given(widgetRepository.findById(upWidgetId)).willReturn(Optional.of(upWidget));

        UpWidgetResDto result = upWidgetService.update(upWidgetId, upWidgetCreateDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(upWidgetCreateDto.getName());
        assertThat(result.getUrl()).isEqualTo(upWidgetCreateDto.getUrl());
    }

    @Test
    void 상단위젯_수정_상단위젯_없을때_예외() {
        given(widgetRepository.findById(upWidgetId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> upWidgetService.update(upWidgetId, upWidgetCreateDto))
                .isInstanceOf(WidgetNotFoundException.class);
    }

    @Test
    void 상단_위젯_삭제() {
        given(widgetRepository.findById(upWidgetId)).willReturn(Optional.of(upWidget));

        upWidgetService.delete(upWidgetId);

        verify(widgetRepository, times(1)).delete(upWidget);
    }

    @Test
    void 상단_위젯_오늘날짜_시간으로_조회() {
        LocalDateTime now = LocalDateTime.now();
        given(widgetRepository.findUpWidgetsByFestivalIdAndPeriod(festivalId, now))
                .willReturn(upWidgetList);

        List<UpWidgetResDto> result = upWidgetService.getUpWidgetsByNow(festivalId, now);
        UpWidgetResDto target = result.get(0);

        UpWidgetProperties props = upWidget.getTypedProperties(UpWidgetProperties.class);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(target.getName()).isEqualTo(upWidget.getName());
        assertThat(target.getUrl()).isEqualTo(upWidget.getUrl());
        assertThat(target.getPeriodStart()).isEqualTo(props.getPeriodStart());
        assertThat(target.getPeriodEnd()).isEqualTo(props.getPeriodEnd());
    }
}
