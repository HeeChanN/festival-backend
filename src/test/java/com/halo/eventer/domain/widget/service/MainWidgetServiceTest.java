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
import com.halo.eventer.domain.widget.WidgetFixture;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetResDto;
import com.halo.eventer.domain.widget.entity.MainWidget;
import com.halo.eventer.domain.widget.exception.WidgetNotFoundException;
import com.halo.eventer.domain.widget.repository.MainWidgetRepository;

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
    private MainWidgetRepository mainWidgetRepository;

    @InjectMocks
    private MainWidgetService mainWidgetService;

    private Festival festival;
    private MainWidget mainWidget;
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
        // given
        final long festivalId = 1L;
        given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
        given(mainWidgetRepository.save(any(MainWidget.class))).willReturn(mainWidget);

        // when
        MainWidgetResDto result = mainWidgetService.create(festivalId, mainWidgetCreateDto);

        // then
        assertResultDtoEqualsMainWidget(result, mainWidget);
    }

    @Test
    void 메인위젯_생성_축제_없을때_예외() {
        // given
        final long festivalId = 1L;
        given(festivalRepository.findById(festivalId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mainWidgetService.create(festivalId, mainWidgetCreateDto))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 메인위젯_리스트_조회() {
        // given
        final long festivalId = 1L;
        given(mainWidgetRepository.findAllByFestivalId(festivalId)).willReturn(List.of(mainWidget));

        // when
        List<MainWidgetResDto> result = mainWidgetService.getAllMainWidget(festivalId);

        // then
        assertThat(result).hasSize(1);
        assertResultDtoEqualsMainWidget(result.get(0), mainWidget);
    }

    @Test
    void 메인위젯_수정_테스트() {
        // given
        given(mainWidgetRepository.findById(mainWidgetId)).willReturn(Optional.of(mainWidget));

        // when
        MainWidgetResDto result = mainWidgetService.update(mainWidgetId, mainWidgetCreateDto);

        // then
        assertResultDtoEqualsMainWidget(result, mainWidget);
    }

    @Test
    void 메인위젯_수정할때_없는경우_예외() {
        // given
        given(mainWidgetRepository.findById(mainWidgetId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> mainWidgetService.update(mainWidgetId, mainWidgetCreateDto))
                .isInstanceOf(WidgetNotFoundException.class);
    }

    @Test
    void 메인위젯_삭제_테스트() {
        // when
        mainWidgetService.delete(mainWidgetId);

        // then
        then(mainWidgetRepository).should().deleteById(mainWidgetId);
    }

    private void assertResultDtoEqualsMainWidget(MainWidgetResDto result, MainWidget mainWidget) {
        assertThat(result.getName()).isEqualTo(mainWidget.getName());
        assertThat(result.getUrl()).isEqualTo(mainWidget.getUrl());
        assertThat(result.getId()).isEqualTo(mainWidget.getId());
        assertThat(result.getImage()).isEqualTo(mainWidget.getImageFeature().getImage());
        assertThat(result.getDescription())
                .isEqualTo(mainWidget.getDescriptionFeature().getDescription());
    }
}
