package com.halo.eventer.domain.home.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.missing_person.MissingPerson;
import com.halo.eventer.domain.missing_person.dto.MissingPersonPopupDto;
import com.halo.eventer.domain.notice.dto.PickedNoticeResDto;
import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.WidgetType;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetResDto;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetResDto;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetResDto;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetResDto;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetResDto;
import com.halo.eventer.domain.widget.properties.UpWidgetProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HomeDto {
    private List<UpWidgetResDto> upWidgets;
    private List<PickedNoticeResDto> banner;
    private List<MainWidgetResDto> mainWidgets;
    private List<MiddleWidgetResDto> middleBanners;
    private List<SquareWidgetResDto> squareWidgets;
    private List<DownWidgetResDto> downWidgets;
    private List<MissingPersonPopupDto> missingPersonDtos;

    public HomeDto(
            List<PickedNoticeResDto> banner, Festival festival, LocalDateTime now, List<MissingPerson> missingPersons) {
        this.banner = banner;
        this.upWidgets = filterUpWidgets(festival.getWidgets(), now);
        this.mainWidgets = filterWidgets(festival.getWidgets(), WidgetType.MAIN, MainWidgetResDto::from);
        this.middleBanners = filterWidgets(festival.getWidgets(), WidgetType.MIDDLE, MiddleWidgetResDto::from);
        this.squareWidgets = filterWidgets(festival.getWidgets(), WidgetType.SQUARE, SquareWidgetResDto::from);
        this.downWidgets = filterWidgets(festival.getWidgets(), WidgetType.DOWN, DownWidgetResDto::from);
        this.missingPersonDtos =
                missingPersons.stream().map(MissingPersonPopupDto::new).collect(Collectors.toList());
    }

    private List<UpWidgetResDto> filterUpWidgets(List<Widget> widgets, LocalDateTime now) {
        return widgets.stream()
                .filter(w -> w.getWidgetType() == WidgetType.UP)
                .filter(w -> {
                    UpWidgetProperties props = w.getTypedProperties(UpWidgetProperties.class);
                    return !now.isBefore(props.getPeriodStart()) && !now.isAfter(props.getPeriodEnd());
                })
                .map(UpWidgetResDto::from)
                .collect(Collectors.toList());
    }

    private <D> List<D> filterWidgets(List<Widget> widgets, WidgetType type, Function<Widget, D> mapper) {
        return widgets.stream()
                .filter(w -> w.getWidgetType() == type)
                .map(mapper)
                .collect(Collectors.toList());
    }
}
