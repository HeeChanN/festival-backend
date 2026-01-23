package com.halo.eventer.domain.stamp.controller.v2;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.stamp.dto.mission.request.*;
import com.halo.eventer.domain.stamp.dto.mission.response.*;
import com.halo.eventer.domain.stamp.dto.stamp.request.PrizeExchangeImgReqDto;
import com.halo.eventer.domain.stamp.service.v2.StampMissionAdminService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin/festivals/{festivalId}/stamp-tours/{stampId}/missions")
public class StampMissionAdminController {

    private final StampMissionAdminService stampMissionAdminService;

    @PostMapping
    public void createMission(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestBody @Valid MissionCreateReqDto request) {
        stampMissionAdminService.createMission(festivalId, stampId, request.getTitle(), request.getShowMission());
    }

    @GetMapping
    public MissionListResDto getMissionList(@PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return stampMissionAdminService.getMissions(festivalId, stampId);
    }

    @DeleteMapping("/{missionId}")
    public void deleteMission(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long missionId) {
        stampMissionAdminService.deleteMission(festivalId, stampId, missionId);
    }

    @PatchMapping("/show")
    public void showMissions(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @Valid @RequestBody MissionsAllShowReqDto request) {
        stampMissionAdminService.showMissionsTrue(festivalId, stampId, request);
    }

    @PatchMapping("/{missionId}/show")
    public void toggleMissionShowing(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long missionId,
            @RequestBody @Valid MissionShowReqDto request) {
        stampMissionAdminService.toggleMissionShowing(festivalId, stampId, missionId, request);
    }

    @GetMapping("/{missionId}/show")
    public MissionShowResDto showMission(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long missionId) {
        return stampMissionAdminService.showMission(festivalId, stampId, missionId);
    }

    @GetMapping("/settings")
    public StampMissionBasicSettingsResDto getBasicSettings(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return stampMissionAdminService.getBasicSettings(festivalId, stampId);
    }

    @PutMapping("/settings")
    public void upsertBasicSettings(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestBody @Valid MissionBasicSettingsReqDto request) {
        stampMissionAdminService.updateBasicSettings(festivalId, stampId, request);
    }

    @GetMapping("/prizes")
    public List<MissionPrizeResDto> getPrizeList(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return stampMissionAdminService.getPrizes(festivalId, stampId);
    }

    @PostMapping("/prizes")
    public void addPrize(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestBody @Valid MissionPrizeCreateReqDto request) {
        stampMissionAdminService.addPrize(festivalId, stampId, request);
    }

    @PutMapping("/prizes/{prizeId}")
    public void updatePrize(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long prizeId,
            @RequestBody @Valid MissionPrizeUpdateReqDto request) {
        stampMissionAdminService.updatePrize(festivalId, stampId, prizeId, request);
    }

    @DeleteMapping("/prizes/{prizeId}")
    public void deletePrize(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long prizeId) {
        stampMissionAdminService.deletePrize(festivalId, stampId, prizeId);
    }

    // TODO : 경품 교환권 이미지 변경 시 기본 이미지 설정 여부 및 기본/커스텀 타입 설정
    @PatchMapping("/prizes/exchange-image")
    public void updateTicketImage(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestBody PrizeExchangeImgReqDto request) {
        stampMissionAdminService.updatePrizeTicketImg(festivalId, stampId, request);
    }

    @GetMapping("/{missionId}/details")
    public MissionDetailsTemplateResDto getMissionDetails(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long missionId) {
        return stampMissionAdminService.getMissionDetailsTemplate(festivalId, stampId, missionId);
    }

    @PutMapping("/{missionId}/details")
    public void upsertTemplate(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long missionId,
            @RequestBody @Valid MissionDetailsTemplateReqDto request) {
        stampMissionAdminService.upsertMissionDetailsTemplate(festivalId, stampId, missionId, request);
    }

    @GetMapping("/{missionId}/clear-img")
    public StampMissionClearImageResDto getMissionClearImg(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long missionId) {
        return stampMissionAdminService.getStampMissionCompleteImage(festivalId, stampId, missionId);
    }

    @PatchMapping("/{missionId}/clear-img")
    public void updateMissionClearImg(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long missionId,
            @RequestBody @Valid StampMissionClearImageReqDto request) {
        stampMissionAdminService.updateStampMissionCompleteImage(festivalId, stampId, missionId, request);
    }

    @GetMapping("/qr")
    public List<MissionQrDataResDto> getMissionsQrData(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return stampMissionAdminService.getMissionsQrData(festivalId, stampId);
    }
}
