package com.halo.eventer.domain.widget.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.WidgetFixture;
import com.halo.eventer.domain.widget.WidgetType;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetResDto;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.properties.MainWidgetProperties;
import com.halo.eventer.domain.widget.repository.WidgetRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class MainWidgetServiceTest {

    @Mock
    private FestivalRepository festivalRepository;

    @Mock
    private WidgetRepository widgetRepository;

    @InjectMocks
    private MainWidgetService mainWidgetService;

    private Festival festival;
    private Widget mainWidget;
    private MainWidgetCreateDto mainWidgetCreateDto;
    final long mainWidgetId = 1L;

    @BeforeEach
    void setUp() {
        festival = FestivalFixture.축제_엔티티();
        mainWidgetCreateDto = WidgetFixture.메인_위젯_생성_DTO();
        mainWidget = WidgetFixture.메인_위젯_엔티티(festival, mainWidgetCreateDto);
        setField(mainWidget, "id", mainWidgetId);
    }

    @Test
    void 메인위젯_생성_테스트() {
        final long festivalId = 1L;
        given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
        given(widgetRepository.save(any(Widget.class))).willReturn(mainWidget);

        MainWidgetResDto result = mainWidgetService.create(festivalId, mainWidgetCreateDto);

        assertResultDtoEqualsWidget(result, mainWidget);
    }

    @Test
    void 메인위젯_생성_축제_없을때_예외() {
        final long festivalId = 1L;
        given(festivalRepository.findById(festivalId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> mainWidgetService.create(festivalId, mainWidgetCreateDto))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 메인위젯_리스트_조회() {
        final long festivalId = 1L;
        given(widgetRepository.findAllByFestivalIdAndWidgetType(festivalId, WidgetType.MAIN))
                .willReturn(List.of(mainWidget));

        List<MainWidgetResDto> result = mainWidgetService.getAllMainWidget(festivalId);

        assertThat(result).hasSize(1);
        assertResultDtoEqualsWidget(result.get(0), mainWidget);
    }

    @Test
    void 메인위젯_수정_테스트() {
        given(widgetRepository.findById(mainWidgetId)).willReturn(Optional.of(mainWidget));

        MainWidgetResDto result = mainWidgetService.update(mainWidgetId, mainWidgetCreateDto);

        assertResultDtoEqualsWidget(result, mainWidget);
    }

    @Test
    void 메인위젯_수정할때_없는경우_예외() {
        given(widgetRepository.findById(mainWidgetId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> mainWidgetService.update(mainWidgetId, mainWidgetCreateDto))
                .isInstanceOf(WidgetNotFoundException.class);
    }

    @Test
    void 메인위젯_삭제_테스트() {
        mainWidgetService.delete(mainWidgetId);

        then(widgetRepository).should().deleteById(mainWidgetId);
    }

    private void assertResultDtoEqualsWidget(MainWidgetResDto result, Widget widget) {
        MainWidgetProperties props = widget.getTypedProperties(MainWidgetProperties.class);
        assertThat(result.getName()).isEqualTo(widget.getName());
        assertThat(result.getUrl()).isEqualTo(widget.getUrl());
        assertThat(result.getId()).isEqualTo(widget.getId());
        assertThat(result.getImage()).isEqualTo(props.getImage());
        assertThat(result.getDescription()).isEqualTo(props.getDescription());
    }
}
