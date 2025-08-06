package com.halo.eventer.domain.home;

import java.time.LocalDateTime;
import java.util.List;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.home.dto.HomeDto;
import com.halo.eventer.domain.missing_person.MissingPerson;
import com.halo.eventer.domain.notice.ArticleType;
import com.halo.eventer.domain.notice.dto.PickedNoticeResDto;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetResDto;
import com.halo.eventer.domain.widget.entity.DownWidget;
import com.halo.eventer.domain.widget.entity.MainWidget;
import com.halo.eventer.domain.widget.entity.MiddleWidget;
import com.halo.eventer.domain.widget.entity.SquareWidget;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class HomeFixture {

    private static Festival festival = FestivalFixture.축제_엔티티();

    private HomeFixture() {}

    public static HomeDto 메인화면_응답() {
        return new HomeDto(banner(), festival(), upWidgets(), missingPersons());
    }

    private static List<PickedNoticeResDto> banner() {
        return List.of(new PickedNoticeResDto(1L, 1, "thumbnail", ArticleType.NOTICE));
    }

    private static List<UpWidgetResDto> upWidgets() {
        return List.of(UpWidgetResDto.of(
                1L,
                "위젯 이름",
                "https://img.test/1.png",
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now(),
                LocalDateTime.now()));
    }

    private static List<MissingPerson> missingPersons() {
        MissingPerson missingPerson =
                MissingPerson.of("name", "20", "남", "thumbnail", "실종 위치", "3시", "내용", "보호자 이름", "보호자 번호", festival);
        return List.of(missingPerson);
    }

    private static Festival festival() {
        MainWidget mainWidget = MainWidget.of(festival, "이름", "url", "image", "설명");
        setField(mainWidget, "id", 1L);
        MiddleWidget middleWidget = MiddleWidget.of(festival, "이름", "url", "image", 1);
        setField(middleWidget, "id", 1L);
        SquareWidget squareWidget = SquareWidget.of(festival, "이름", "url", "image", "설명", 1);
        setField(squareWidget, "id", 1L);
        DownWidget downWidget = DownWidget.of(festival, "이름", "url", 1);
        setField(downWidget, "id", 1L);
        return festival;
    }
}
