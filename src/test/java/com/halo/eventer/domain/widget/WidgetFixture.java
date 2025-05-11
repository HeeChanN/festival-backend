package com.halo.eventer.domain.widget;

import java.time.LocalDateTime;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetCreateDto;
import com.halo.eventer.domain.widget.entity.*;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class WidgetFixture {

    public static SquareWidget 정사각형_위젯_엔티티(Festival festival, SquareWidgetCreateDto dto) {
        return SquareWidget.from(festival, dto);
    }

    public static SquareWidgetCreateDto 정사각형_위젯_생성_DTO() {
        SquareWidgetCreateDto dto = new SquareWidgetCreateDto();
        setField(dto, "name", "name");
        setField(dto, "description", "description");
        setField(dto, "image", "image");
        setField(dto, "url", "url");
        return dto;
    }

    public static SquareWidgetCreateDto 정사각형_위젯_수정_DTO() {
        SquareWidgetCreateDto dto = new SquareWidgetCreateDto();
        setField(dto, "name", "update_name");
        setField(dto, "description", "update_description");
        setField(dto, "image", "update_image");
        setField(dto, "url", "update_url");
        return dto;
    }

    public static MiddleWidget 중간_위젯_엔티티(Festival festival, MiddleWidgetCreateDto dto) {
        return MiddleWidget.from(festival, dto);
    }

    public static MiddleWidgetCreateDto 중간_위젯_생성_DTO() {
        MiddleWidgetCreateDto dto = new MiddleWidgetCreateDto();
        setField(dto, "name", "name");
        setField(dto, "image", "image");
        setField(dto, "url", "url");
        return dto;
    }

    public static MiddleWidgetCreateDto 중간_위젯_수정_DTO() {
        MiddleWidgetCreateDto dto = new MiddleWidgetCreateDto();
        setField(dto, "name", "update_name");
        setField(dto, "image", "update_image");
        setField(dto, "url", "update_url");
        return dto;
    }

    public static MainWidget 메인_위젯_엔티티(Festival festival, MainWidgetCreateDto dto) {
        return MainWidget.from(festival, dto);
    }

    public static MainWidgetCreateDto 메인_위젯_생성_DTO() {
        MainWidgetCreateDto dto = new MainWidgetCreateDto();
        setField(dto, "name", "name");
        setField(dto, "description", "description");
        setField(dto, "image", "image");
        setField(dto, "url", "url");
        return dto;
    }

    public static MainWidgetCreateDto 메인_위젯_수정_DTO() {
        MainWidgetCreateDto dto = new MainWidgetCreateDto();
        setField(dto, "name", "update_name");
        setField(dto, "description", "update_description");
        setField(dto, "image", "update_image");
        setField(dto, "url", "update_url");
        return dto;
    }

    public static DownWidget 하단_위젯_엔티티(Festival festival, DownWidgetCreateDto dto) {
        return DownWidget.from(festival, dto);
    }

    public static DownWidgetCreateDto 하단_위젯_생성_DTO() {
        DownWidgetCreateDto dto = new DownWidgetCreateDto();
        setField(dto, "name", "name");
        setField(dto, "url", "url");
        return dto;
    }

    public static DownWidgetCreateDto 하단_위젯_수정_DTO() {
        DownWidgetCreateDto dto = new DownWidgetCreateDto();
        setField(dto, "name", "update_name");
        setField(dto, "url", "update_url");
        return dto;
    }

    public static UpWidget 상단_위젯_엔티티(Festival festival, UpWidgetCreateDto dto) {
        return UpWidget.from(festival, dto);
    }

    public static UpWidgetCreateDto 상단_위젯_생성_DTO() {
        UpWidgetCreateDto dto = new UpWidgetCreateDto();
        setField(dto, "name", "name");
        setField(dto, "url", "url");
        setField(dto, "periodStart", LocalDateTime.now().minusMonths(1));
        setField(dto, "periodEnd", LocalDateTime.now().plusMonths(1));
        return dto;
    }
}
