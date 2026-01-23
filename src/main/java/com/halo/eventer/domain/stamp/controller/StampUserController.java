package com.halo.eventer.domain.stamp.controller;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.stamp.dto.stamp.StampUsersGetDto;
import com.halo.eventer.domain.stamp.dto.stampUser.*;
import com.halo.eventer.domain.stamp.service.StampUserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stamp/user")
@Hidden
public class StampUserController {
    private final StampUserService stampUserService;

    @PostMapping
    public StampUserGetDto signup(
            @Min(1) @RequestParam("stampId") Long stampId,
            @RequestBody @Valid SignupWithoutCustomDto signupWithoutCustomDto) {
        return stampUserService.signup(stampId, signupWithoutCustomDto);
    }

    @PostMapping("/custom")
    public StampUserGetDto signupWithCustom(
            @Min(1) @RequestParam("stampId") Long stampId,
            @RequestBody @Valid SignupWithCustomDto signupWithCustomDto) {
        return stampUserService.signup(stampId, signupWithCustomDto);
    }

    @PostMapping("/signup")
    public StampUserGetDto signup(
            @Min(1) @RequestParam("stampId") long stampId, @RequestBody @Valid SignupDto signupDto) {
        return stampUserService.signupV2(stampId, signupDto);
    }

    @PostMapping("/login")
    public StampUserGetDto login(@Min(1) @RequestParam("stampId") long stampId, @RequestBody @Valid LoginDto loginDto) {
        return stampUserService.login(stampId, loginDto);
    }

    @GetMapping("/{uuid}")
    public UserMissionInfoWithFinishedGetListDto getMissionInfo(@Size(min = 36, max = 36) @PathVariable String uuid) {
        return stampUserService.getUserMissionWithFinished(uuid);
    }

    @GetMapping("/all")
    public List<StampUsersGetDto> getStampUsers(@Min(1) @RequestParam("stampId") long stampId) {
        return stampUserService.getStampUsers(stampId);
    }

    @PatchMapping("/{uuid}/{userMissionId}")
    public void updateUserMission(
            @Size(min = 36, max = 36) @PathVariable String uuid, @Min(1) @PathVariable long userMissionId) {
        stampUserService.updateUserMission(uuid, userMissionId);
    }

    @PatchMapping("/check/{uuid}")
    public String checkFinish(@Size(min = 36, max = 36) @PathVariable String uuid) {
        return stampUserService.checkFinish(uuid);
    }

    @PatchMapping("/check/v2/{uuid}")
    public String checkV2Finish(@Size(min = 36, max = 36) @PathVariable String uuid) {
        return stampUserService.checkV2Finish(uuid);
    }

    @DeleteMapping("/{uuid}")
    public void deleteStampByUuid(@Size(min = 36, max = 36) @PathVariable String uuid) {
        stampUserService.deleteStampByUuid(uuid);
    }
}
