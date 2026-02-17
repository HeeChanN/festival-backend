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
import com.halo.eventer.global.cache.DistributedCacheManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {

    private static final String HOME_CACHE_KEY_PREFIX = "home:";

    private final NoticeService noticeService;
    private final FestivalRepository festivalRepository;
    private final MissingPersonService missingPersonService;
    private final DistributedCacheManager distributedCacheManager;

    @Transactional(readOnly = true)
    public HomeDto getMainPage(Long festivalId) {
        return distributedCacheManager.get(HOME_CACHE_KEY_PREFIX + festivalId, () -> loadHomeDto(festivalId));
    }

    private HomeDto loadHomeDto(Long festivalId) {
        Festival festival = getFestival(festivalId);
        return new HomeDto(getBanner(festivalId), festival, LocalDateTime.now(), getMissingPersons(festivalId));
    }

    private List<PickedNoticeResDto> getBanner(Long festivalId) {
        return noticeService.getPickedNotice(festivalId);
    }

    private Festival getFestival(Long festivalId) {
        return festivalRepository
                .findByIdWithWidgetsWithinPeriod(festivalId)
                .orElseThrow(() -> new FestivalNotFoundException(festivalId));
    }

    private List<MissingPerson> getMissingPersons(Long festivalId) {
        return missingPersonService.getPopupList(festivalId);
    }
}
