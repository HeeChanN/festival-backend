package com.halo.eventer.domain.stamp.service.v2;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.stamp.*;
import com.halo.eventer.domain.stamp.dto.mission.response.MissionPrizeResDto;
import com.halo.eventer.domain.stamp.dto.stamp.enums.PageType;
import com.halo.eventer.domain.stamp.dto.stamp.request.StampTourSignUpTemplateResDto;
import com.halo.eventer.domain.stamp.dto.stamp.response.*;
import com.halo.eventer.domain.stamp.exception.*;
import com.halo.eventer.domain.stamp.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.halo.eventer.domain.stamp.dto.stamp.enums.PageType.LANDING;
import static com.halo.eventer.domain.stamp.dto.stamp.enums.PageType.MAIN;

@Service
@RequiredArgsConstructor
@Slf4j
public class StampTourTemplateService {

    private final FestivalRepository festivalRepository;
    private final StampRepository stampRepository;
    private final PageTemplateRepository pageTemplateRepository;
    private final StampNoticeRepository stampNoticeRepository;
    private final ParticipateGuideRepository participateGuideRepository;

    @Transactional(readOnly = true)
    public List<StampTourSummaryResDto> getStampTourList(long festivalId) {
        Festival festival = loadFestivalOrThrow(festivalId);
        List<Stamp> stamps = filterStampsOnlyShowing(festival);
        return StampTourSummaryResDto.fromEntities(stamps);
    }

    @Transactional(readOnly = true)
    public StampTourSignUpTemplateResDto getSignupTemplate(long festivalId, long stampTourId) {
        Stamp stamp = ensureStamp(festivalId, stampTourId);
        return StampTourSignUpTemplateResDto.from(stamp);
    }

    @Transactional(readOnly = true)
    public StampTourLandingPageResDto getLandingPage(long festivalId, long stampTourId) {
        Stamp stamp = ensureStamp(festivalId, stampTourId);
        PageTemplate landingPage = loadPageTemplateOrThrow(stampTourId, LANDING);
        return StampTourLandingPageResDto.from(landingPage);
    }

    @Transactional(readOnly = true)
    public StampTourMainPageResDto getMainPage(long festivalId, long stampTourId) {
        Stamp stamp = ensureStamp(festivalId, stampTourId);
        PageTemplate mainPage = loadPageTemplateOrThrow(stampTourId, MAIN);
        return StampTourMainPageResDto.from(mainPage);
    }

    @Transactional(readOnly = true)
    public StampTourJoinTemplateResDto getStampTourJoinMethod(long festivalId, long stampTourId) {
        Stamp stamp = ensureStamp(festivalId, stampTourId);
        return StampTourJoinTemplateResDto.from(stamp.getJoinVerificationMethod());
    }

    @Transactional(readOnly = true)
    public StampTourAuthMethodResDto getAuthMethod(long festivalId, long stampTourId) {
        Stamp stamp = ensureStamp(festivalId, stampTourId);
        return new StampTourAuthMethodResDto(stamp.getAuthMethod());
    }

    @Transactional(readOnly = true)
    public StampTourNotificationResDto getStampTourNotification(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        var existing = stampNoticeRepository.findAllByStampIdOrderByIdDesc(stampId);
        return StampTourNotificationResDto.from(existing.get(0));
    }

    @Transactional(readOnly = true)
    public StampTourGuideResDto getParticipateGuide(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        ParticipateGuide guide = loadParticipateGuideOrThrow(stampId);
        return StampTourGuideResDto.from(guide);
    }

    @Transactional(readOnly = true)
    public List<MissionPrizeResDto> getPrizes(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        List<StampMissionPrize> prizes = stamp.getPrizes();
        return MissionPrizeResDto.fromEntities(prizes);
    }

    @Transactional(readOnly = true)
    public StampActiveResDto getStampActive(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        return new StampActiveResDto(stamp.getTitle(), stamp.getActive());
    }

    @Transactional(readOnly = true)
    public PrizeExchangeImgResDto getStampTourPrizeExchangeImg(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        return PrizeExchangeImgResDto.from(stamp);
    }

    private List<Stamp> filterStampsOnlyShowing(Festival festival) {
        return festival.getStamps().stream().filter(Stamp::getShowStamp).toList();
    }

    private Festival loadFestivalOrThrow(long id) {
        return festivalRepository.findById(id).orElseThrow(() -> new FestivalNotFoundException(id));
    }

    private Stamp ensureStamp(long festivalId, long stampId) {
        Stamp stamp = loadStampOrThrow(stampId);
        stamp.ensureStampInFestival(festivalId);
        return stamp;
    }

    private Stamp loadStampOrThrow(long stampId) {
        return stampRepository.findById(stampId).orElseThrow(() -> new StampNotFoundException(stampId));
    }

    private PageTemplate loadPageTemplateOrThrow(long stampId, PageType pageType) {
        return pageTemplateRepository
                .findFirstByStampIdAndType(stampId, pageType)
                .orElseThrow(() -> new PageTemplateNotFoundException(stampId));
    }

    private ParticipateGuide loadParticipateGuideOrThrow(long stampId) {
        return participateGuideRepository
                .findFirstByStampId(stampId)
                .orElseThrow(() -> new ParticipateGuideNotFoundException(stampId));
    }
}
