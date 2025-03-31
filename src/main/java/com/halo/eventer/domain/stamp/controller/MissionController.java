package com.halo.eventer.domain.stamp.controller;

import com.halo.eventer.domain.stamp.dto.mission.MissionDetailGetDto;
import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.service.MissionService;
import com.halo.eventer.domain.stamp.swagger.MissionDetailGetApi;
import com.halo.eventer.domain.stamp.swagger.MissionUpdateApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "스탬프 투어 - 미션")
@RequiredArgsConstructor
@RestController
@RequestMapping("/stamp/mission")
public class MissionController {

  private final MissionService missionService;

  @MissionDetailGetApi
  @GetMapping
  public MissionDetailGetDto getMission(@RequestParam("missionId") Long missionId) {
    return missionService.getMission(missionId);
  }

  @MissionUpdateApi
  @PatchMapping
  public void updateMission(
          @RequestParam("missionId") Long missionId,
          @RequestBody MissionUpdateDto missionUpdateDto) {
    missionService.updateMission(missionId, missionUpdateDto);
  }
}
