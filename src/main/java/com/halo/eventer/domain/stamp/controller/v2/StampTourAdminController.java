package com.halo.eventer.domain.stamp.controller.v2;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.stamp.dto.stamp.request.*;
import com.halo.eventer.domain.stamp.dto.stamp.response.*;
import com.halo.eventer.domain.stamp.service.v2.StampTourAdminService;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin/festivals/{festivalId}/stamp-tours")
public class StampTourAdminController {

    private final StampTourAdminService stampTourAdminService;

    @PostMapping
    public void createStampTour(
            @PathVariable @Min(1) long festivalId, @RequestBody @Valid StampTourCreateReqDto request) {
        stampTourAdminService.createStampTourByFestival(festivalId, request);
    }

    @GetMapping
    public List<StampTourSummaryResDto> getStampToursList(@PathVariable @Min(1) long festivalId) {
        return stampTourAdminService.getStampTourListByFestival(festivalId);
    }

    @GetMapping("/{stampId}/settings/user-info")
    public StampTourJoinVerificationResDto getStampTourVerificationMethod(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return stampTourAdminService.getJoinVerification(festivalId, stampId);
    }

    @PatchMapping("/{stampId}/settings/user-info")
    public void updateStampTourVerificationMethod(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @Valid @RequestBody StampTourJoinConditionReqDto request) {
        stampTourAdminService.updateJoinCondition(festivalId, stampId, request);
    }

    @DeleteMapping("/{stampId}")
    public void deleteStampTour(@PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        stampTourAdminService.deleteStampTour(festivalId, stampId);
    }

    @PatchMapping("/{stampId}/showStamp")
    public void toggleStampTourShow(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestBody @Valid StampTourShowToggleReqDto request) {
        stampTourAdminService.toggleShowStamp(festivalId, stampId, request.isShowStamp());
    }

    @GetMapping("/{stampId}/settings/basic")
    public StampTourSettingBasicResDto getSettingBasicForStampTour(
            @PathVariable long festivalId, @PathVariable long stampId) {
        return stampTourAdminService.getStampTourSettingBasicByFestival(festivalId, stampId);
    }

    @PatchMapping("/{stampId}/settings/basic")
    public void upsertBasicSettingsForStampTour(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestBody @Valid StampTourBasicUpdateReqDto request) {
        stampTourAdminService.updateBasicSettings(festivalId, stampId, request);
    }

    @GetMapping("/{stampId}/settings/notice")
    public StampTourNotificationResDto getNotificationForStampTour(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return stampTourAdminService.getStampTourNotification(festivalId, stampId);
    }

    @PutMapping("/{stampId}/settings/notice")
    public void upsertNotificationForStampTour(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestBody @Valid NotificationContentReqDto request) {
        stampTourAdminService.updateStampTourNotification(
                festivalId, stampId, request.getCautionContent(), request.getPersonalInformationContent());
    }

    @GetMapping("/{stampId}/settings/landing")
    public StampTourLandingPageResDto getLandingPageForStampTour(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return stampTourAdminService.getLandingPageSettings(festivalId, stampId);
    }

    @PutMapping("/{stampId}/settings/landing")
    public void upsertLandingPageForStampTour(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestBody @Valid StampTourLandingPageReqDto request) {
        stampTourAdminService.updateLandingPage(festivalId, stampId, request);
    }

    @GetMapping("/{stampId}/settings/main")
    public StampTourMainPageResDto getMainPageForStampTour(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return stampTourAdminService.getMainPageSettings(festivalId, stampId);
    }

    @PutMapping("/{stampId}/settings/main")
    public void upsertMainPageForStampTour(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestBody @Valid StampTourMainPageReqDto request) {
        stampTourAdminService.updateMainPageSettings(festivalId, stampId, request);
    }

    @GetMapping("/{stampId}/settings/guides")
    public StampTourParticipateGuideResDto getParticipateIntroductionFromStampTour(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return stampTourAdminService.getParticipateGuide(festivalId, stampId);
    }

    @PatchMapping("/{stampId}/settings/guides")
    public List<ParticipateGuidePageSummaryResDto> updateDisplayOrder(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestBody List<OrderUpdateRequest> requests) {
        return stampTourAdminService.updateDisplayOrder(festivalId, stampId, requests);
    }

    @PutMapping("/{stampId}/settings/guides")
    public void upsertParticipateIntroductionFromStampTour(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestBody @Valid StampTourParticipateGuideReqDto request) {
        stampTourAdminService.updateParticipateGuide(festivalId, stampId, request);
    }

    @DeleteMapping("/{stampId}/settings/guides/pages/{pageId}")
    public void deleteParticipateIntroductionPageFromStampTour(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long pageId) {
        stampTourAdminService.deleteParticipateGuidePage(festivalId, stampId, pageId);
    }

    @PostMapping("/{stampId}/settings/guides/pages")
    public void createParticipationGuidePage(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestBody @Valid StampTourParticipateGuidePageReqDto request) {
        stampTourAdminService.createParticipateGuidePage(festivalId, stampId, request);
    }

    @GetMapping("/{stampId}/settings/guides/pages/{pageId}")
    public ParticipateGuidePageDetailsResDto getParticipationGuidePage(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long pageId) {
        return stampTourAdminService.getParticipateGuidePageDetails(festivalId, stampId, pageId);
    }

    @PutMapping("/{stampId}/settings/guides/pages/{pageId}")
    public void upsertParticipateGuidePageFromStampTour(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long pageId,
            @RequestBody @Valid StampTourParticipateGuidePageReqDto request) {
        stampTourAdminService.updateParticipateGuidePage(festivalId, stampId, pageId, request);
    }

    @GetMapping("/{stampId}")
    public StampActiveResDto getStampActive(@PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return stampTourAdminService.getStampActive(festivalId, stampId);
    }
}
