package com.halo.eventer.domain.widget.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.widget.WidgetFixture;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetCreateDto;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class SquareWidgetTest {

    private Festival festival;
    private SquareWidget squareWidget;
    private SquareWidgetCreateDto squareWidgetCreateDto;

    @BeforeEach
    void setUp() {
        festival = FestivalFixture.축제_엔티티();
        squareWidgetCreateDto = WidgetFixture.정사각형_위젯_생성_DTO();
        squareWidget = WidgetFixture.정사각형_위젯_엔티티(festival, squareWidgetCreateDto);
    }

    @Test
    void SquareWidget_생성_테스트() {
        // when
        SquareWidget result = WidgetFixture.정사각형_위젯_엔티티(festival, squareWidgetCreateDto);

        // then
        assertThat(result.getName()).isEqualTo(squareWidgetCreateDto.getName());
        assertThat(result.getDescriptionFeature().getDescription()).isEqualTo(squareWidgetCreateDto.getDescription());
        assertThat(result.getImageFeature().getImage()).isEqualTo(squareWidgetCreateDto.getImage());
        assertThat(result.getUrl()).isEqualTo(squareWidgetCreateDto.getUrl());
    }

    @Test
    void SquareWidget_수정_테스트() {
        // when
        squareWidgetCreateDto = WidgetFixture.정사각형_위젯_수정_DTO();
        squareWidget.updateSquareWidget(squareWidgetCreateDto);

        // then
        assertThat(squareWidget.getName()).isEqualTo(squareWidgetCreateDto.getName());
        assertThat(squareWidget.getDescriptionFeature().getDescription())
                .isEqualTo(squareWidgetCreateDto.getDescription());
        assertThat(squareWidget.getImageFeature().getImage()).isEqualTo(squareWidgetCreateDto.getImage());
        assertThat(squareWidget.getUrl()).isEqualTo(squareWidgetCreateDto.getUrl());
    }

    @Test
    void SquareWidget_DisplayOrder_수정_테스트() {
        // given
        Integer displayOrder = 1;

        // when
        squareWidget.updateDisplayOrder(displayOrder);

        // then
        assertThat(squareWidget.getDisplayOrderFeature().getDisplayOrder()).isEqualTo(displayOrder);
    }
}
