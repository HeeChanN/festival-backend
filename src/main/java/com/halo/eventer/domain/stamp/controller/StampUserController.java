package com.halo.eventer.domain.stamp.controller;

import com.halo.eventer.domain.stamp.dto.stampUser.*;
import com.halo.eventer.domain.stamp.service.StampUserService;
import com.halo.eventer.domain.stamp.swagger.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "스탬프 투어 - 유저")
@RestController
@RequiredArgsConstructor
@RequestMapping("/stamp/user")
public class StampUserController {
    private final StampUserService stampUserService;

    /** 스탬프 유저 생성 - custom X */
    @SignupApi
    @PostMapping
    public StampUserGetDto signup(@RequestParam("stampId") Long stampId, @RequestBody SignupWithoutCustomDto signupWithoutCustomDto) {
        return stampUserService.signup(stampId, signupWithoutCustomDto);
    }

    /** 스탬프 유저 생성 - custom O */
    @SignupApi
    @PostMapping("/custom")
    public StampUserGetDto signupWithCustom(@RequestParam("stampId") Long stampId, @RequestBody SignupWithCustomDto signupWithCustomDto) {
        return stampUserService.signup(stampId, signupWithCustomDto);
    }

    /** 로그인 */
    @LoginApi
    @PostMapping("/login")
    public StampUserGetDto login(@RequestParam("stampId") Long stampId, @RequestBody LoginDto loginDto) {
        return stampUserService.login(stampId, loginDto);
    }

    /** 유저 미션 전체 조회 */
    @MissionInfoGetApi
    @GetMapping("/{uuid}")
    public UserMissionInfoGetListDto getMissionInfo(@PathVariable String uuid) {
        return stampUserService.getUserMission(uuid);
    }

    /** 사용자 미션 상태 업데이트 */
    @PatchMapping("/{uuid}/{userMissionId}")
    public String updateUserMission(@PathVariable String uuid, @PathVariable Long userMissionId) {
        return stampUserService.updateUserMission(uuid, userMissionId);
    }

    /** 사용자 미션 완료 상태 확인 */
    @StampCheckApi
    @PatchMapping("/check/{uuid}")
    public String checkFinish(@PathVariable String uuid) {
        return stampUserService.checkFinish(uuid);
    }

    /** 사용자 삭제 */
    @DeleteMapping("/{uuid}")
    public String deleteStampByUuid(@PathVariable String uuid) {
        return stampUserService.deleteStampByUuid(uuid);
    }
}
