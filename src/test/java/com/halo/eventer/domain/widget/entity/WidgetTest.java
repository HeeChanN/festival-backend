package com.halo.eventer.domain.widget.entity;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.WidgetFixture;
import com.halo.eventer.domain.widget.WidgetType;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetCreateDto;
import com.halo.eventer.domain.widget.properties.*;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class WidgetTest {

    private Festival festival;

    @BeforeEach
    void setUp() {
        festival = FestivalFixture.축제_엔티티();
    }

    @Test
    void MainWidget_생성_테스트() {
        MainWidgetCreateDto dto = WidgetFixture.메인_위젯_생성_DTO();
        Widget widget = WidgetFixture.메인_위젯_엔티티(festival, dto);

        assertThat(widget.getName()).isEqualTo(dto.getName());
        assertThat(widget.getUrl()).isEqualTo(dto.getUrl());
        assertThat(widget.getWidgetType()).isEqualTo(WidgetType.MAIN);
        MainWidgetProperties props = widget.getTypedProperties(MainWidgetProperties.class);
        assertThat(props.getDescription()).isEqualTo(dto.getDescription());
        assertThat(props.getImage()).isEqualTo(dto.getImage());
    }

    @Test
    void MainWidget_수정_테스트() {
        MainWidgetCreateDto dto = WidgetFixture.메인_위젯_생성_DTO();
        Widget widget = WidgetFixture.메인_위젯_엔티티(festival, dto);

        MainWidgetCreateDto updateDto = WidgetFixture.메인_위젯_수정_DTO();
        widget.updateBaseField(updateDto.getName(), updateDto.getUrl());
        widget.updateProperties(new MainWidgetProperties(updateDto.getImage(), updateDto.getDescription()));

        assertThat(widget.getName()).isEqualTo(updateDto.getName());
        assertThat(widget.getUrl()).isEqualTo(updateDto.getUrl());
        MainWidgetProperties props = widget.getTypedProperties(MainWidgetProperties.class);
        assertThat(props.getDescription()).isEqualTo(updateDto.getDescription());
        assertThat(props.getImage()).isEqualTo(updateDto.getImage());
    }

    @Test
    void UpWidget_생성_테스트() {
        LocalDateTime now = LocalDateTime.now();
        Widget widget = Widget.createUpWidget(festival, "이름", "url", now, now);

        assertThat(widget.getWidgetType()).isEqualTo(WidgetType.UP);
        assertThat(widget.getName()).isEqualTo("이름");
        UpWidgetProperties props = widget.getTypedProperties(UpWidgetProperties.class);
        assertThat(props.getPeriodStart()).isEqualTo(now);
        assertThat(props.getPeriodEnd()).isEqualTo(now);
    }

    @Test
    void UpWidget_수정_테스트() {
        UpWidgetCreateDto dto = WidgetFixture.상단_위젯_생성_DTO();
        Widget widget = WidgetFixture.상단_위젯_엔티티(festival, dto);
        LocalDateTime now = LocalDateTime.now();

        widget.updateBaseField("이름1", "url1");
        widget.updateProperties(new UpWidgetProperties(now, now));

        assertThat(widget.getName()).isEqualTo("이름1");
        assertThat(widget.getUrl()).isEqualTo("url1");
        UpWidgetProperties props = widget.getTypedProperties(UpWidgetProperties.class);
        assertThat(props.getPeriodStart()).isEqualTo(now);
        assertThat(props.getPeriodEnd()).isEqualTo(now);
    }

    @Test
    void MiddleWidget_생성_테스트() {
        MiddleWidgetCreateDto dto = WidgetFixture.중간_위젯_생성_DTO();
        Widget widget = WidgetFixture.중간_위젯_엔티티(festival, dto);

        assertThat(widget.getWidgetType()).isEqualTo(WidgetType.MIDDLE);
        assertThat(widget.getName()).isEqualTo(dto.getName());
        MiddleWidgetProperties props = widget.getTypedProperties(MiddleWidgetProperties.class);
        assertThat(props.getImage()).isEqualTo(dto.getImage());
    }

    @Test
    void MiddleWidget_순서_변경_테스트() {
        MiddleWidgetCreateDto dto = WidgetFixture.중간_위젯_생성_DTO();
        Widget widget = WidgetFixture.중간_위젯_엔티티(festival, dto);

        widget.updateDisplayOrder(1);
        assertThat(widget.getDisplayOrder()).isEqualTo(1);
    }

    @Test
    void SquareWidget_생성_테스트() {
        SquareWidgetCreateDto dto = WidgetFixture.정사각형_위젯_생성_DTO();
        Widget widget = WidgetFixture.정사각형_위젯_엔티티(festival, dto);

        assertThat(widget.getWidgetType()).isEqualTo(WidgetType.SQUARE);
        assertThat(widget.getName()).isEqualTo(dto.getName());
        SquareWidgetProperties props = widget.getTypedProperties(SquareWidgetProperties.class);
        assertThat(props.getDescription()).isEqualTo(dto.getDescription());
        assertThat(props.getImage()).isEqualTo(dto.getImage());
    }

    @Test
    void SquareWidget_DisplayOrder_수정_테스트() {
        SquareWidgetCreateDto dto = WidgetFixture.정사각형_위젯_생성_DTO();
        Widget widget = WidgetFixture.정사각형_위젯_엔티티(festival, dto);

        widget.updateDisplayOrder(1);
        assertThat(widget.getDisplayOrder()).isEqualTo(1);
    }

    @Test
    void DownWidget_생성_테스트() {
        DownWidgetCreateDto dto = WidgetFixture.하단_위젯_생성_DTO();
        Widget widget = WidgetFixture.하단_위젯_엔티티(festival, dto);

        assertThat(widget.getWidgetType()).isEqualTo(WidgetType.DOWN);
        assertThat(widget.getName()).isEqualTo(dto.getName());
        assertThat(widget.getUrl()).isEqualTo(dto.getUrl());
    }

    @Test
    void DownWidget_DisplayOrder_수정_테스트() {
        DownWidgetCreateDto dto = WidgetFixture.하단_위젯_생성_DTO();
        Widget widget = WidgetFixture.하단_위젯_엔티티(festival, dto);

        widget.updateDisplayOrder(1);
        assertThat(widget.getDisplayOrder()).isEqualTo(1);
    }
}
