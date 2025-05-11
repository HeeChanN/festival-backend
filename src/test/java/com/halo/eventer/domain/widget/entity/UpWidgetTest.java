package com.halo.eventer.domain.widget.entity;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetCreateDto;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class UpWidgetTest {

    private Festival festival;
    private UpWidgetCreateDto upWidgetCreateDto;

    @BeforeEach
    void setUp() {
        // Mock 데이터 초기화
        festival = new Festival();
        upWidgetCreateDto = UpWidgetCreateDto.of("이름", "url", LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void of_생성_테스트() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // When
        UpWidget upWidget = UpWidget.of(festival, "이름", "url", now, now);

        // Then
        assertThat(upWidget).isNotNull();
        assertThat(upWidget.getFestival()).isEqualTo(festival);
        assertThat(upWidget.getName()).isEqualTo("이름");
        assertThat(upWidget.getUrl()).isEqualTo("url");
        assertThat(upWidget.getPeriodFeature().getPeriodStart()).isEqualTo(now);
        assertThat(upWidget.getPeriodFeature().getPeriodEnd()).isEqualTo(now);
    }

    @Test
    void from_생성_테스트() {
        // When
        UpWidget upWidget = UpWidget.from(festival, upWidgetCreateDto);

        // Then
        assertThat(upWidget).isNotNull();
        assertThat(upWidget.getFestival()).isEqualTo(festival);
        assertThat(upWidget.getName()).isEqualTo(upWidgetCreateDto.getName());
        assertThat(upWidget.getUrl()).isEqualTo(upWidgetCreateDto.getUrl());
        assertThat(upWidget.getPeriodFeature().getPeriodStart()).isEqualTo(upWidgetCreateDto.getPeriodStart());
        assertThat(upWidget.getPeriodFeature().getPeriodEnd()).isEqualTo(upWidgetCreateDto.getPeriodEnd());
    }

    @Test
    void updateUpWidgetTest() {
        // Given
        UpWidget upWidget = UpWidget.from(festival, upWidgetCreateDto);
        LocalDateTime now = LocalDateTime.now();

        // When
        UpWidgetCreateDto updatedDto = UpWidgetCreateDto.of("이름1", "url1", now, now);
        upWidget.updateUpWidget(updatedDto);

        // Then
        assertThat(upWidget.getName()).isEqualTo("이름1");
        assertThat(upWidget.getUrl()).isEqualTo("url1");
        assertThat(upWidget.getPeriodFeature().getPeriodStart()).isEqualTo(now);
        assertThat(upWidget.getPeriodFeature().getPeriodEnd()).isEqualTo(now);
    }
}
