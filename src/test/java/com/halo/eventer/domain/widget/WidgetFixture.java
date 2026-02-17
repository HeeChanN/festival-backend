package com.halo.eventer.domain.widget;

import java.time.LocalDateTime;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetCreateDto;

import static com.halo.eventer.global.constants.DisplayOrderConstants.DISPLAY_ORDER_DEFAULT;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class WidgetFixture {

    public static Widget 정사각형_위젯_엔티티(Festival festival, SquareWidgetCreateDto dto) {
        return Widget.createSquareWidget(
                festival, dto.getName(), dto.getUrl(), dto.getImage(), dto.getDescription(), DISPLAY_ORDER_DEFAULT);
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

    public static Widget 중간_위젯_엔티티(Festival festival, MiddleWidgetCreateDto dto) {
        return Widget.createMiddleWidget(festival, dto.getName(), dto.getUrl(), dto.getImage(), DISPLAY_ORDER_DEFAULT);
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

    public static Widget 메인_위젯_엔티티(Festival festival, MainWidgetCreateDto dto) {
        return Widget.createMainWidget(festival, dto.getName(), dto.getUrl(), dto.getImage(), dto.getDescription());
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

    public static Widget 하단_위젯_엔티티(Festival festival, DownWidgetCreateDto dto) {
        return Widget.createDownWidget(festival, dto.getName(), dto.getUrl(), DISPLAY_ORDER_DEFAULT);
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

    public static Widget 상단_위젯_엔티티(Festival festival, UpWidgetCreateDto dto) {
        return Widget.createUpWidget(festival, dto.getName(), dto.getUrl(), dto.getPeriodStart(), dto.getPeriodEnd());
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
