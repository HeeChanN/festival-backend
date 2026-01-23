package com.halo.eventer.domain.stamp.service.v2;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.stamp.*;
import com.halo.eventer.domain.stamp.dto.stamp.enums.PageType;
import com.halo.eventer.domain.stamp.dto.stamp.request.*;
import com.halo.eventer.domain.stamp.dto.stamp.response.*;
import com.halo.eventer.domain.stamp.exception.PageTemplateNotFoundException;
import com.halo.eventer.domain.stamp.exception.ParticipateGuideNotFoundException;
import com.halo.eventer.domain.stamp.exception.ParticipateGuidePageNotFoundException;
import com.halo.eventer.domain.stamp.exception.StampNotFoundException;
import com.halo.eventer.domain.stamp.repository.*;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.global.util.DisplayOrderUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.halo.eventer.domain.stamp.dto.stamp.enums.PageType.LANDING;
import static com.halo.eventer.domain.stamp.dto.stamp.enums.PageType.MAIN;

@Service
@RequiredArgsConstructor
@Slf4j
public class StampTourAdminService {

    private final FestivalRepository festivalRepository;
    private final StampRepository stampRepository;
    private final StampNoticeRepository stampNoticeRepository;
    private final PageTemplateRepository pageTemplateRepository;
    private final ParticipateGuideRepository participateGuideRepository;
    private final ParticipateGuidePageRepository participateGuidePageRepository;

    @Transactional
    public void createStampTourByFestival(long festivalId, StampTourCreateReqDto request) {
        Festival festival = loadFestivalOrThrow(festivalId);
        Stamp stamp = Stamp.createWith(festival, request.getTitle(), request.getShowStamp());
        stampRepository.save(stamp);
    }

    @Transactional(readOnly = true)
    public List<StampTourSummaryResDto> getStampTourListByFestival(long festivalId) {
        Festival festival = loadFestivalOrThrow(festivalId);
        return StampTourSummaryResDto.fromEntities(festival.getStamps());
    }

    @Transactional
    public void deleteStampTour(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        stampRepository.delete(stamp);
    }

    @Transactional
    public void toggleShowStamp(long festivalId, long stampId, boolean showStamp) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        stamp.toggleShowStamp(showStamp);
    }

    @Transactional
    public StampTourJoinVerificationResDto getJoinVerification(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        return StampTourJoinVerificationResDto.from(stamp.getJoinVerificationMethod());
    }

    @Transactional
    public void updateJoinCondition(long festivalId, long stampId, StampTourJoinConditionReqDto request) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        stamp.updateJoinCondition(
                request.getJoinVerificationMethod(), request.getMaxParticipantCount(), request.getExtraInfoTemplate());
    }

    @Transactional(readOnly = true)
    public StampTourSettingBasicResDto getStampTourSettingBasicByFestival(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        return StampTourSettingBasicResDto.from(stamp);
    }

    @Transactional
    public void updateBasicSettings(long festivalId, long stampId, final StampTourBasicUpdateReqDto request) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        stamp.changeBasicSettings(
                request.getStampActivate(),
                request.getTitle(),
                request.getAuthMethod(),
                request.getPrizeReceiptAuthPassword(),
                request.getMainColor(),
                request.getSubColor(),
                request.getBackgroundColor(),
                request.getBackgroundSubColor());
    }

    @Transactional
    public StampTourNotificationResDto getStampTourNotification(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        var existing = stampNoticeRepository.findAllByStampIdOrderByIdDesc(stampId);
        if (!existing.isEmpty()) {
            return StampTourNotificationResDto.from(existing.get(0));
        }
        StampNotice created = StampNotice.defaultNotice(stamp);
        stampNoticeRepository.save(created);
        return StampTourNotificationResDto.from(created);
    }

    @Transactional
    public void updateStampTourNotification(
            long festivalId, long stampId, String cautionContent, String personalInformationContent) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        StampNotice stampNotice =
                stampNoticeRepository.findAllByStampIdOrderByIdDesc(stampId).get(0); // TODO : 하드 코딩 수정 필요
        stampNotice.update(cautionContent, personalInformationContent);
    }

    @Transactional
    public StampTourLandingPageResDto getLandingPageSettings(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        PageTemplate pageTemplate = pageTemplateRepository
                .findFirstByStampIdAndType(stampId, LANDING)
                .orElseGet(() -> createNewLandingTemplate(stamp));
        return StampTourLandingPageResDto.from(pageTemplate);
    }

    @Transactional
    public void updateLandingPage(long festivalId, long stampId, StampTourLandingPageReqDto request) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        PageTemplate pageTemplate = loadPageTemplateOrThrow(stampId, LANDING);
        pageTemplate.updateLandingPageTemplate(request);
    }

    @Transactional
    public StampTourMainPageResDto getMainPageSettings(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        PageTemplate pageTemplate = pageTemplateRepository
                .findFirstByStampIdAndType(stampId, MAIN)
                .orElseGet(() -> createNewMainTemplate(stamp));
        return StampTourMainPageResDto.from(pageTemplate);
    }

    @Transactional
    public void updateMainPageSettings(long festivalId, long stampId, StampTourMainPageReqDto request) {
        ensureStamp(festivalId, stampId);
        PageTemplate pageTemplate = loadPageTemplateOrThrow(stampId, MAIN);
        pageTemplate.updateMainPageTemplate(request);
    }

    @Transactional
    public StampTourParticipateGuideResDto getParticipateGuide(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        ParticipateGuide guide = participateGuideRepository
                .findFirstByStampId(stampId)
                .orElseGet(() -> createNewParticipateGuide(stamp));
        return StampTourParticipateGuideResDto.from(guide);
    }

    @Transactional
    public void updateParticipateGuide(long festivalId, long stampId, StampTourParticipateGuideReqDto request) {
        ensureStamp(festivalId, stampId);
        ParticipateGuide guide = loadParticipateGuideOrThrow(stampId);
        guide.update(request.getGuideDesignTemplate(), request.getGuideSlideMethod());
    }

    @Transactional
    public void deleteParticipateGuidePage(long festivalId, long stampId, long pageId) {
        ensureStamp(festivalId, stampId);
        ParticipateGuidePage page = loadParticipateGuidePageOrThrow(pageId);
        participateGuidePageRepository.delete(page);
    }

    @Transactional
    public void createParticipateGuidePage(long festivalId, long stampId, StampTourParticipateGuidePageReqDto request) {
        ensureStamp(festivalId, stampId);
        ParticipateGuide guide = loadParticipateGuideOrThrow(stampId);
        ParticipateGuidePage page = ParticipateGuidePage.from(
                request.getTitle(),
                request.getMediaSpec(),
                request.getMediaUrl(),
                request.getSummary(),
                request.getDetails(),
                request.getAdditional(),
                guide);
        participateGuidePageRepository.save(page);
    }

    @Transactional(readOnly = true)
    public ParticipateGuidePageDetailsResDto getParticipateGuidePageDetails(
            long festivalId, long stampId, long pageId) {
        ensureStamp(festivalId, stampId);
        ParticipateGuidePage page = loadParticipateGuidePageOrThrow(pageId);
        return ParticipateGuidePageDetailsResDto.from(page);
    }

    @Transactional
    public void updateParticipateGuidePage(
            long festivalId, long stampId, long pageId, StampTourParticipateGuidePageReqDto request) {
        ensureStamp(festivalId, stampId);
        ParticipateGuidePage page = loadParticipateGuidePageOrThrow(pageId);
        page.update(request);
    }

    @Transactional
    public List<ParticipateGuidePageSummaryResDto> updateDisplayOrder(
            long festivalId, long stampId, List<OrderUpdateRequest> orderRequests) {
        ensureStamp(festivalId, stampId);
        ParticipateGuide guide = loadParticipateGuideOrThrow(stampId);
        List<ParticipateGuidePage> pages = guide.getParticipateGuidePages();
        DisplayOrderUtils.updateDisplayOrder(pages, orderRequests);
        return pages.stream().map(ParticipateGuidePageSummaryResDto::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StampActiveResDto getStampActive(long festivalId, long stampId) {
        Stamp stamp = ensureStamp(festivalId, stampId);
        return new StampActiveResDto(stamp.getTitle(), stamp.getActive());
    }

    private PageTemplate createNewLandingTemplate(Stamp stamp) {
        PageTemplate newPageTemplate = PageTemplate.defaultLandingPage(stamp);
        return pageTemplateRepository.save(newPageTemplate);
    }

    private PageTemplate createNewMainTemplate(Stamp stamp) {
        PageTemplate newPageTemplate = PageTemplate.defaultMainPage(stamp);
        return pageTemplateRepository.save(newPageTemplate);
    }

    private ParticipateGuide createNewParticipateGuide(Stamp stamp) {
        ParticipateGuide participationGuide = ParticipateGuide.defaultParticipateGuide(stamp);
        return participateGuideRepository.save(participationGuide);
    }

    private Stamp ensureStamp(long festivalId, long stampId) {
        Stamp stamp = loadStampOrThrow(stampId);
        stamp.ensureStampInFestival(festivalId);
        return stamp;
    }

    private Festival loadFestivalOrThrow(long id) {
        return festivalRepository.findById(id).orElseThrow(() -> new FestivalNotFoundException(id));
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

    private ParticipateGuidePage loadParticipateGuidePageOrThrow(long pageId) {
        return participateGuidePageRepository
                .findById(pageId)
                .orElseThrow(() -> new ParticipateGuidePageNotFoundException(pageId));
    }
}
