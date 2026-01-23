package com.halo.eventer.domain.stamp.controller.v2;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.stamp.dto.mission.request.MissionClearReqDto;
import com.halo.eventer.domain.stamp.dto.stampUser.enums.Finished;
import com.halo.eventer.domain.stamp.dto.stampUser.enums.SortType;
import com.halo.eventer.domain.stamp.dto.stampUser.request.MissionCompletionUpdateReq;
import com.halo.eventer.domain.stamp.dto.stampUser.request.StampUserInfoUpdateReqDto;
import com.halo.eventer.domain.stamp.dto.stampUser.response.*;
import com.halo.eventer.domain.stamp.service.v2.StampUserAdminService;
import com.halo.eventer.global.common.page.PagedResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin/festivals/{festivalId}/stamp-tours/{stampId}/users")
public class StampUserAdminController {

    private final StampUserAdminService stampUserAdminService;

    @GetMapping("/all")
    public List<StampUserInfoResDto> getAllStampUsers(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId) {
        return stampUserAdminService.getAllStampUsers(festivalId, stampId);
    }

    @GetMapping
    public PagedResponse<StampUserSummaryResDto> getStampUsersFiltered(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestParam(defaultValue = "", required = false) String q,
            @RequestParam(defaultValue = "ALL", required = false) Finished finished,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "NAME", required = false) SortType sortType) {
        return stampUserAdminService.getStampUsers(festivalId, stampId, q, finished, page, size, sortType);
    }

    @PatchMapping("/{userId}/tour-finish")
    public void updateStampUser(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long userId,
            @RequestBody @Valid MissionCompletionUpdateReq req) {
        stampUserAdminService.updateStampUserPrizeAndFinished(festivalId, stampId, userId, req);
    }

    @GetMapping("/{userId}")
    public StampUserDetailResDto getStampUser(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long userId) {
        return stampUserAdminService.getUserDetail(festivalId, stampId, userId);
    }

    @PatchMapping("/{userId}/user-mission/{userMissionId}")
    public void updateStampUserMission(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long userId,
            @PathVariable @Min(1) long userMissionId,
            @RequestBody @Valid MissionClearReqDto request) {
        stampUserAdminService.updateUserMissionState(festivalId, stampId, userId, userMissionId, request);
    }

    @PatchMapping("/{userId}")
    public void updateStampUserInfo(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long userId,
            @RequestBody @Valid StampUserInfoUpdateReqDto request) {
        stampUserAdminService.updateStampUserInfo(festivalId, stampId, userId, request);
    }

    @GetMapping("/uuid/{uuid}")
    public StampUserUserIdResDto getStampUserUuid(
            @PathVariable @Min(1) long festivalId, @PathVariable @Min(1) long stampId, @PathVariable String uuid) {
        return stampUserAdminService.getStampUserId(festivalId, stampId, uuid);
    }
}
