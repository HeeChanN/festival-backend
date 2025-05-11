package com.halo.eventer.domain.widget.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.widget.WidgetFixture;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetCreateDto;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class MainWidgetTest {

    private Festival festival;
    private MainWidget mainWidget;
    private MainWidgetCreateDto mainWidgetCreateDto;

    @BeforeEach
    void setUp() {
        festival = FestivalFixture.축제_엔티티();
        mainWidgetCreateDto = WidgetFixture.메인_위젯_생성_DTO();
        mainWidget = MainWidget.from(festival, mainWidgetCreateDto);
    }

    @Test
    void MainWidget_생성_테스트() {
        // when
        MainWidget result = WidgetFixture.메인_위젯_엔티티(festival, mainWidgetCreateDto);

        // then
        assertThat(result.getName()).isEqualTo(mainWidgetCreateDto.getName());
        assertThat(result.getDescriptionFeature().getDescription()).isEqualTo(mainWidgetCreateDto.getDescription());
        assertThat(result.getImageFeature().getImage()).isEqualTo(mainWidgetCreateDto.getImage());
        assertThat(result.getUrl()).isEqualTo(mainWidgetCreateDto.getUrl());
    }

    @Test
    void MainWidget_수정_테스트() {
        // when
        mainWidgetCreateDto = WidgetFixture.메인_위젯_수정_DTO();
        mainWidget.updateMainWidget(mainWidgetCreateDto);

        // then
        assertThat(mainWidget.getName()).isEqualTo(mainWidgetCreateDto.getName());
        assertThat(mainWidget.getDescriptionFeature().getDescription()).isEqualTo(mainWidgetCreateDto.getDescription());
        assertThat(mainWidget.getImageFeature().getImage()).isEqualTo(mainWidgetCreateDto.getImage());
        assertThat(mainWidget.getUrl()).isEqualTo(mainWidgetCreateDto.getUrl());
    }
}
