package com.halo.eventer.domain.widget.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.widget.WidgetFixture;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetCreateDto;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class MiddleWidgetTest {

    private Festival festival;
    private MiddleWidgetCreateDto middleWidgetCreateDto;
    private MiddleWidget middleWidget;

    @BeforeEach
    void setUp() {
        festival = FestivalFixture.축제_엔티티();
        middleWidgetCreateDto = WidgetFixture.중간_위젯_생성_DTO();
        middleWidget = WidgetFixture.중간_위젯_엔티티(festival, middleWidgetCreateDto);
    }

    @Test
    void MiddleWidget_생성_테스트() {
        // when
        MiddleWidget middleWidget = MiddleWidget.from(festival, middleWidgetCreateDto);

        // then
        assertThat(middleWidget.getName()).isEqualTo(middleWidgetCreateDto.getName());
        assertThat(middleWidget.getImageFeature().getImage()).isEqualTo(middleWidgetCreateDto.getImage());
        assertThat(middleWidget.getUrl()).isEqualTo(middleWidgetCreateDto.getUrl());
    }

    @Test
    void MiddleWidget_수정_테스트() {
        // when
        MiddleWidgetCreateDto updateDto = WidgetFixture.중간_위젯_수정_DTO();
        middleWidget.updateMiddleWidget(updateDto);

        // then
        assertThat(middleWidget.getName()).isEqualTo(updateDto.getName());
        assertThat(middleWidget.getImageFeature().getImage()).isEqualTo(updateDto.getImage());
        assertThat(middleWidget.getUrl()).isEqualTo(updateDto.getUrl());
    }

    @Test
    void MiddleWidget_순서_변경_테스트() {
        // given
        Integer displayOrder = 1;

        // when
        middleWidget.updateDisplayOrder(displayOrder);

        // then
        assertThat(middleWidget.getDisplayOrderFeature().getDisplayOrder()).isEqualTo(displayOrder);
    }
}
