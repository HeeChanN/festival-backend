package com.halo.eventer.domain.stamp.controller;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.dto.mission.request.MissionSetReqDto;
import com.halo.eventer.domain.stamp.dto.mission.response.MissionDetailGetResDto;
import com.halo.eventer.domain.stamp.dto.mission.response.MissionSummaryResDto;
import com.halo.eventer.domain.stamp.service.MissionService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stamp/mission")
@Hidden
public class MissionController {

    private final MissionService missionService;

    @PostMapping
    public void createMissionList(
            @Min(1) @RequestParam("stampId") Long stampId,
            @RequestBody @Valid @NotEmpty List<@Valid MissionSetReqDto> dto) {
        missionService.createMission(stampId, dto);
    }

    @GetMapping
    public MissionDetailGetResDto getMission(@Min(1) @RequestParam("missionId") Long missionId) {
        return missionService.getMission(missionId);
    }

    @GetMapping("/all")
    public List<MissionSummaryResDto> getMissionList(@Min(1) @RequestParam("stampId") Long stampId) {
        return missionService.getMissions(stampId);
    }

    @PatchMapping
    public void updateMission(
            @Min(1) @RequestParam("missionId") Long missionId, @RequestBody MissionUpdateDto missionUpdateDto) {
        missionService.updateMission(missionId, missionUpdateDto);
    }
}
