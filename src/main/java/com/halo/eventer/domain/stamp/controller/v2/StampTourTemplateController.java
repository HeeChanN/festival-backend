package com.halo.eventer.domain.stamp.controller.v2;

import java.util.List;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.halo.eventer.domain.stamp.dto.mission.response.MissionPrizeResDto;
import com.halo.eventer.domain.stamp.dto.stamp.request.StampTourSignUpTemplateResDto;
import com.halo.eventer.domain.stamp.dto.stamp.response.*;
import com.halo.eventer.domain.stamp.service.v2.StampTourTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/template/festivals/{festivalId}/stamp-tours")
@Slf4j
public class StampTourTemplateController {

    private final StampTourTemplateService templateService;

    @GetMapping
    public List<StampTourSummaryResDto> getStampTourList(@PathVariable @Min(1) long festivalId) {
        return templateService.getStampTourList(festivalId);
    }

    @GetMapping("/{stampId}/signup")
    public StampTourSignUpTemplateResDto getSignUpSettings(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return templateService.getSignupTemplate(festivalId, stampId);
    }

    @GetMapping("/{stampId}/landing")
    public StampTourLandingPageResDto getStampTourLandingPage(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return templateService.getLandingPage(festivalId, stampId);
    }

    @GetMapping("/{stampId}/join-template")
    public StampTourJoinTemplateResDto getStampUserJoinTemplate(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return templateService.getStampTourJoinMethod(festivalId, stampId);
    }

    @GetMapping("/{stampId}/auth-method")
    public StampTourAuthMethodResDto getStampTourAuthMethod(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return templateService.getAuthMethod(festivalId, stampId);
    }

    @GetMapping("/{stampId}/main")
    public StampTourMainPageResDto getStampTourMainPage(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return templateService.getMainPage(festivalId, stampId);
    }

    @GetMapping("/{stampId}/notice")
    public StampTourNotificationResDto getStampTourNotification(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return templateService.getStampTourNotification(festivalId, stampId);
    }

    @GetMapping("/{stampId}/guide")
    public StampTourGuideResDto getStampTourParticipationGuide(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return templateService.getParticipateGuide(festivalId, stampId);
    }

    @GetMapping("/{stampId}/prizes")
    public List<MissionPrizeResDto> getPrizeList(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return templateService.getPrizes(festivalId, stampId);
    }

    @GetMapping("/{stampId}")
    public StampActiveResDto getStampActive(@PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return templateService.getStampActive(festivalId, stampId);
    }

    @GetMapping("/{stampId}/prizes/exchange-image")
    public PrizeExchangeImgResDto getStampTourPrizeExchangeImg(
            @PathVariable long festivalId, @PathVariable @Min(1) long stampId) {
        return templateService.getStampTourPrizeExchangeImg(festivalId, stampId);
    }
}
