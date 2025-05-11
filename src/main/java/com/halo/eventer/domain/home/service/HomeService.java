package com.halo.eventer.domain.home.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.home.dto.HomeDto;
import com.halo.eventer.domain.missing_person.MissingPerson;
import com.halo.eventer.domain.missing_person.service.MissingPersonService;
import com.halo.eventer.domain.notice.dto.PickedNoticeResDto;
import com.halo.eventer.domain.notice.service.NoticeService;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetResDto;
import com.halo.eventer.domain.widget.service.UpWidgetService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final NoticeService noticeService;
    private final FestivalRepository festivalRepository;
    private final MissingPersonService missingPersonService;
    private final UpWidgetService upWidgetService;

    @Transactional(readOnly = true)
    public HomeDto getMainPage(Long festivalId, LocalDateTime dateTime) {
        List<PickedNoticeResDto> banner = noticeService.getPickedNotice(festivalId);
        List<UpWidgetResDto> upWidgets = upWidgetService.getUpWidgetsByNow(festivalId, dateTime);
        Festival festival = festivalRepository
                .findByIdWithWidgetsWithinPeriod(festivalId)
                .orElseThrow(() -> new FestivalNotFoundException(festivalId));
        List<MissingPerson> missingPersonList = missingPersonService.getPopupList(festivalId);
        return new HomeDto(banner, festival, upWidgets, missingPersonList);
    }
}
