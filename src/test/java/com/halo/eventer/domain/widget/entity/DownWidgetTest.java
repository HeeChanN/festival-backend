package com.halo.eventer.domain.widget.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.widget.WidgetFixture;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetCreateDto;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class DownWidgetTest {

    private Festival festival;
    private DownWidget downWidget;
    private DownWidgetCreateDto downWidgetCreateDto;

    @BeforeEach
    void setUp() {
        festival = FestivalFixture.축제_엔티티();
        downWidgetCreateDto = WidgetFixture.하단_위젯_생성_DTO();
        downWidget = WidgetFixture.하단_위젯_엔티티(festival, downWidgetCreateDto);
    }

    @Test
    void DownWidget_생성_테스트() {
        // when
        DownWidget result = WidgetFixture.하단_위젯_엔티티(festival, downWidgetCreateDto);

        // then
        assertThat(result.getName()).isEqualTo(downWidgetCreateDto.getName());
        assertThat(result.getUrl()).isEqualTo(downWidgetCreateDto.getUrl());
    }

    @Test
    void DownWidget_수정_테스트() {
        // when
        downWidgetCreateDto = WidgetFixture.하단_위젯_수정_DTO();
        downWidget.updateDownWidget(downWidgetCreateDto);

        // then
        assertThat(downWidget.getName()).isEqualTo(downWidgetCreateDto.getName());
        assertThat(downWidget.getUrl()).isEqualTo(downWidgetCreateDto.getUrl());
    }

    @Test
    void DownWidget_DisplayOrder_수정_테스트() {
        // given
        Integer displayOrder = 1;

        // when
        downWidget.updateDisplayOrder(displayOrder);

        // then
        assertThat(downWidget.getDisplayOrderFeature().getDisplayOrder()).isEqualTo(displayOrder);
    }
}
