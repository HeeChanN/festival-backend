package com.halo.eventer.domain.home.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.missing_person.MissingPerson;
import com.halo.eventer.domain.missing_person.dto.MissingPersonPopupDto;
import com.halo.eventer.domain.notice.dto.PickedNoticeResDto;
import com.halo.eventer.domain.widget.BaseWidget;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetResDto;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetResDto;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetResDto;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetResDto;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetResDto;
import com.halo.eventer.domain.widget.entity.*;
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
            List<PickedNoticeResDto> banner,
            Festival festival,
            List<UpWidgetResDto> upWidgets,
            List<MissingPerson> missingPersons) {
        this.upWidgets = upWidgets;
        this.banner = banner;
        this.mainWidgets = filterWidgets(festival.getBaseWidgets(), MainWidget.class, MainWidgetResDto::from);
        this.middleBanners = filterWidgets(festival.getBaseWidgets(), MiddleWidget.class, MiddleWidgetResDto::from);
        this.squareWidgets = filterWidgets(festival.getBaseWidgets(), SquareWidget.class, SquareWidgetResDto::from);
        this.downWidgets = filterWidgets(festival.getBaseWidgets(), DownWidget.class, DownWidgetResDto::from);
        this.missingPersonDtos =
                missingPersons.stream().map(MissingPersonPopupDto::new).collect(Collectors.toList());
    }

    private <E extends BaseWidget, D> List<D> filterWidgets(
            List<BaseWidget> widgets, Class<E> type, Function<E, D> mapper) {
        return widgets.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .map(mapper)
                .collect(Collectors.toList());
    }
}
