package com.halo.eventer.domain.widget.service;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;

import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetResDto;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.repository.UpWidgetRepository;
import com.halo.eventer.domain.widget.entity.UpWidget;
import com.halo.eventer.domain.widget.util.WidgetPageHelper;
import com.halo.eventer.global.common.page.PageInfo;
import com.halo.eventer.global.common.sort.SortOption;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class UpSquareWidgetServiceTest {

    @Mock
    private FestivalRepository festivalRepository;

    @Mock
    private UpWidgetRepository upWidgetRepository;

    @Mock
    private WidgetPageHelper widgetPageHelper;

    @InjectMocks
    private UpWidgetService upWidgetService;

    private Festival festival;
    private UpWidget upWidget;
    private UpWidgetCreateDto upWidgetCreateDto;
    private Pageable pageable;
    private List<UpWidget> upWidgetList;
    private Page<UpWidget> upWidgetPage;
    private PagedResponse<UpWidgetResDto> pagedResponse;
    private PageInfo pageInfo;

    private final long festivalId= 1L;
    private final long upWidgetId= 1L;

    @BeforeEach
    void setUp(){
        festival = new Festival();
        upWidget = UpWidget.of(festival, "이름", "url", LocalDateTime.now(),LocalDateTime.now());
        upWidgetCreateDto = UpWidgetCreateDto.of("이름", "url", LocalDateTime.now(),LocalDateTime.now());
        pageable = PageRequest.of(0, 10);
        upWidgetList = Collections.singletonList(upWidget);
        upWidgetPage = new PageImpl<>(upWidgetList, pageable, upWidgetList.size());
        pageInfo = PageInfo.builder()
                .pageNumber(upWidgetPage.getNumber())
                .pageSize(upWidgetPage.getSize())
                .totalElements(upWidgetPage.getTotalElements())
                .totalPages(upWidgetPage.getTotalPages())
                .build();
        pagedResponse = new PagedResponse<UpWidgetResDto>(
            upWidgetList.stream().map(UpWidgetResDto::from).collect(Collectors.toList()), pageInfo);
    }

    @Test
    void 상단위젯_생성(){
        //given
        given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
        given(upWidgetRepository.save(any())).willReturn(upWidget);

        //when
        UpWidgetResDto result = upWidgetService.create(festivalId,upWidgetCreateDto);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(upWidget.getName());
        assertThat(result.getUrl()).isEqualTo(upWidget.getUrl());
        assertThat(result.getPeriodStart()).isEqualTo(upWidget.getPeriodFeature().getPeriodStart());
        assertThat(result.getPeriodEnd()).isEqualTo(upWidget.getPeriodFeature().getPeriodEnd());
    }

    @Test
    void 상단위젯_생성_축제_없을때_예외(){
        // given
        given(festivalRepository.findById(festivalId)).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> upWidgetService.create(festivalId,upWidgetCreateDto))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 상단위젯_단일_조회(){
        //given
        given(upWidgetRepository.findById(upWidgetId)).willReturn(Optional.of(upWidget));

        //when
        UpWidgetResDto result = upWidgetService.getUpWidget(upWidgetId);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(upWidget.getName());
        assertThat(result.getUrl()).isEqualTo(upWidget.getUrl());
        assertThat(result.getPeriodStart()).isEqualTo(upWidget.getPeriodFeature().getPeriodStart());
        assertThat(result.getPeriodEnd()).isEqualTo(upWidget.getPeriodFeature().getPeriodEnd());
    }

    @Test
    void 상단위젯_없을때_예외(){
        // given
        given(upWidgetRepository.findById(upWidgetId)).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> upWidgetService.getUpWidget(upWidgetId))
                .isInstanceOf(WidgetNotFoundException.class);
    }

    @Test
    void 상단위젯_번호_페이징_조회(){
        //given
        given(widgetPageHelper.findWidgetsBySort(UpWidget.class,festivalId,SortOption.CREATED_AT,pageable)).willReturn(upWidgetPage);
        given(festivalRepository.existsById(festivalId)).willReturn(true);
        given(widgetPageHelper.getPagedResponse(any(),any(Function.class))).willReturn(pagedResponse);

        //when
        PagedResponse<UpWidgetResDto> result = upWidgetService.getUpWidgetsWithOffsetPaging(festivalId, SortOption.CREATED_AT, 0, 10);
        UpWidgetResDto target = result.getContent().get(0);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(target.getName()).isEqualTo(upWidget.getName());
        assertThat(target.getUrl()).isEqualTo(upWidget.getUrl());
        assertThat(target.getPeriodStart()).isEqualTo(upWidget.getPeriodFeature().getPeriodStart());
        assertThat(target.getPeriodEnd()).isEqualTo(upWidget.getPeriodFeature().getPeriodEnd());
    }

    @Test
    void 상단위젯_번호_페이징_축제_없을때_예외(){
        // given
        given(festivalRepository.existsById(festivalId)).willReturn(false);


        //when & then
        assertThatThrownBy(() -> upWidgetService.getUpWidgetsWithOffsetPaging(festivalId, SortOption.CREATED_AT,0,10))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 상단위젯_수정(){
        //given
        given(upWidgetRepository.findById(upWidgetId)).willReturn(Optional.of(upWidget));

        //when
        UpWidgetResDto result = upWidgetService.update(upWidgetId,upWidgetCreateDto);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(upWidget.getName());
        assertThat(result.getUrl()).isEqualTo(upWidget.getUrl());
        assertThat(result.getPeriodStart()).isEqualTo(upWidget.getPeriodFeature().getPeriodStart());
        assertThat(result.getPeriodEnd()).isEqualTo(upWidget.getPeriodFeature().getPeriodEnd());
    }

    @Test
    void 상단위젯_수정_상단위젯_없을때_예외(){
        //given
        given(upWidgetRepository.findById(upWidgetId)).willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> upWidgetService.update(upWidgetId,upWidgetCreateDto))
                .isInstanceOf(WidgetNotFoundException.class);
    }

    @Test
    void 상단_위젯_삭제(){
        //given
        doNothing().when(upWidgetRepository).deleteById(upWidgetId);

        //when
        upWidgetService.delete(upWidgetId);

        //then
        verify(upWidgetRepository, times(1)).deleteById(upWidgetId);
    }

    @Test
    void 상단_위젯_오늘날짜_시간으로_조회(){
        //given
        LocalDateTime now = LocalDateTime.now();
        given(upWidgetRepository.findAllByFestivalIdAndPeriod(festivalId,now)).willReturn(upWidgetList);

        //when
        List<UpWidgetResDto> result = upWidgetService.getUpWidgetsByNow(festivalId,now);
        UpWidgetResDto target = result.get(0);
        //then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(target.getName()).isEqualTo(upWidget.getName());
        assertThat(target.getUrl()).isEqualTo(upWidget.getUrl());
        assertThat(target.getPeriodStart()).isEqualTo(upWidget.getPeriodFeature().getPeriodStart());
        assertThat(target.getPeriodEnd()).isEqualTo(upWidget.getPeriodFeature().getPeriodEnd());
    }
}
