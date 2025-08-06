package com.halo.eventer.domain.stamp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.stamp.dto.mission.MissionDetailGetDto;
import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.dto.stamp.MissionSetDto;
import com.halo.eventer.domain.stamp.dto.stamp.MissionSummaryGetDto;
import com.halo.eventer.domain.stamp.service.MissionService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stamp/mission")
public class MissionController {

    private final MissionService missionService;

    @PostMapping
    public void createMissionList(@RequestParam("stampId") Long stampId, @RequestBody List<MissionSetDto> dto) {
        missionService.createMission(stampId, dto);
    }

    @GetMapping
    public MissionDetailGetDto getMission(@RequestParam("missionId") Long missionId) {
        return missionService.getMission(missionId);
    }

    @GetMapping("/all")
    public List<MissionSummaryGetDto> getMissionList(@RequestParam("stampId") Long stampId) {
        return missionService.getMissions(stampId);
    }

    @PatchMapping
    public void updateMission(
            @RequestParam("missionId") Long missionId, @RequestBody MissionUpdateDto missionUpdateDto) {
        missionService.updateMission(missionId, missionUpdateDto);
    }
}
