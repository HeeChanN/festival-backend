package com.halo.eventer.domain.stamp.controller.v2;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.member.dto.TokenDto;
import com.halo.eventer.domain.stamp.dto.mission.response.MissionBoardResDto;
import com.halo.eventer.domain.stamp.dto.mission.response.MissionTemplateResDto;
import com.halo.eventer.domain.stamp.dto.stampUser.request.MissionQrVerifyReqDto;
import com.halo.eventer.domain.stamp.dto.stampUser.request.StampUserLoginDto;
import com.halo.eventer.domain.stamp.dto.stampUser.request.StampUserSignupReqDto;
import com.halo.eventer.domain.stamp.dto.stampUser.response.PrizeClaimQrResDto;
import com.halo.eventer.domain.stamp.service.v2.StampTourUserService;
import com.halo.eventer.global.security.CustomStampUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/user/festivals/{festivalId}/stamp-tours")
@Slf4j
public class StampTourUserController {

    private final StampTourUserService stampTourUserService;

    @PostMapping("/{stampId}/signup")
    public void stampUserSignup(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestBody @Valid StampUserSignupReqDto request) {
        stampTourUserService.signup(festivalId, stampId, request);
    }

    @PostMapping("/{stampId}/login")
    public TokenDto login(
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestBody @Valid StampUserLoginDto request) {
        return stampTourUserService.login(festivalId, stampId, request);
    }

    @GetMapping("/{stampId}/missions")
    public MissionBoardResDto getUserMissionBoard(
            @AuthenticationPrincipal CustomStampUserDetails userDetails,
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId) {
        return stampTourUserService.getMissionBoard(festivalId, stampId, userDetails.getUsername());
    }

    @GetMapping("/{stampId}/missions/{missionId}")
    public MissionTemplateResDto getMissionDetails(
            @AuthenticationPrincipal CustomStampUserDetails userDetails,
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @PathVariable @Min(1) long missionId) {
        return stampTourUserService.getMissionsTemplate(festivalId, stampId, missionId, userDetails.getUsername());
    }

    // TODO : QR 인증방식 변경 필요 : 토큰 방식 고려 중
    @PatchMapping("/{stampId}/verify/qr")
    public void successMissionVerifyByQr(
            @AuthenticationPrincipal CustomStampUserDetails userDetails,
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId,
            @RequestBody @Valid MissionQrVerifyReqDto request) {
        stampTourUserService.successMissionByQr(festivalId, stampId, request.getMissionId(), userDetails.getUsername());
    }

    @GetMapping("/{stampId}/prizes/qr")
    public PrizeClaimQrResDto getStampUserPrizeQrInfo(
            @AuthenticationPrincipal CustomStampUserDetails userDetails,
            @PathVariable @Min(1) long festivalId,
            @PathVariable @Min(1) long stampId) {
        return stampTourUserService.getPrizeReceiveQr(festivalId, stampId, userDetails.getUsername());
    }
}
