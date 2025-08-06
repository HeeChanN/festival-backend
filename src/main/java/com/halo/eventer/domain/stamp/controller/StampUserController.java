package com.halo.eventer.domain.stamp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.stamp.dto.stamp.StampUsersGetDto;
import com.halo.eventer.domain.stamp.dto.stampUser.*;
import com.halo.eventer.domain.stamp.service.StampUserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stamp/user")
public class StampUserController {
    private final StampUserService stampUserService;

    @PostMapping
    public StampUserGetDto signup(
            @RequestParam("stampId") Long stampId, @RequestBody SignupWithoutCustomDto signupWithoutCustomDto) {
        return stampUserService.signup(stampId, signupWithoutCustomDto);
    }

    @PostMapping("/custom")
    public StampUserGetDto signupWithCustom(
            @RequestParam("stampId") Long stampId, @RequestBody SignupWithCustomDto signupWithCustomDto) {
        return stampUserService.signup(stampId, signupWithCustomDto);
    }

    @PostMapping("/signup")
    public StampUserGetDto signup(@RequestParam("stampId") Long stampId, @RequestBody SignupDto signupDto) {
        return stampUserService.signupV2(stampId, signupDto);
    }

    @PostMapping("/login")
    public StampUserGetDto login(@RequestParam("stampId") Long stampId, @RequestBody LoginDto loginDto) {
        return stampUserService.login(stampId, loginDto);
    }

    @GetMapping("/{uuid}")
    public UserMissionInfoWithFinishedGetListDto getMissionInfo(@PathVariable String uuid) {
        return stampUserService.getUserMissionWithFinished(uuid);
    }

    @GetMapping("/all")
    public List<StampUsersGetDto> getStampUsers(@RequestParam("stampId") Long stampId) {
        return stampUserService.getStampUsers(stampId);
    }

    @PatchMapping("/{uuid}/{userMissionId}")
    public void updateUserMission(@PathVariable String uuid, @PathVariable Long userMissionId) {
        stampUserService.updateUserMission(uuid, userMissionId);
    }

    @PatchMapping("/check/{uuid}")
    public String checkFinish(@PathVariable String uuid) {
        return stampUserService.checkFinish(uuid);
    }

    @PatchMapping("/check/v2/{uuid}")
    public String checkV2Finish(@PathVariable String uuid) {
        return stampUserService.checkV2Finish(uuid);
    }

    @DeleteMapping("/{uuid}")
    public void deleteStampByUuid(@PathVariable String uuid) {
        stampUserService.deleteStampByUuid(uuid);
    }
}
