package com.halo.eventer.domain.stamp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.stamp.dto.stamp.*;
import com.halo.eventer.domain.stamp.service.StampService;
import com.halo.eventer.domain.stamp.swagger.MissionListGetApi;
import com.halo.eventer.domain.stamp.swagger.MissionListSetApi;
import com.halo.eventer.domain.stamp.swagger.StampOnUpdateApi;
import com.halo.eventer.domain.stamp.swagger.StampUsersListGetApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "스탬프 투어")
@RestController
@RequiredArgsConstructor
@RequestMapping("/stamp")
public class StampController {

    private final StampService stampService;

    @PostMapping
    public List<StampGetDto> registerStamp(@RequestParam("festivalId") Long festivalId) {
        return stampService.registerStamp(festivalId);
    }

    @GetMapping
    public List<StampGetDto> getStampList(@RequestParam("festivalId") Long festivalId) {
        return stampService.getStampByFestivalId(festivalId);
    }

    @StampOnUpdateApi
    @PatchMapping
    public void updateStampOn(@RequestParam("stampId") Long stampId) {
        stampService.updateStampOn(stampId);
    }

    @DeleteMapping
    public void deleteStamp(@RequestParam("stampId") Long stampId) {
        stampService.deleteStamp(stampId);
    }

    @MissionListSetApi
    @PostMapping("/mission")
    public void setMissionList(@RequestParam("stampId") Long stampId, @RequestBody MissionSetListDto dto) {
        stampService.createMission(stampId, dto);
    }

    @MissionListGetApi
    @GetMapping("/missions")
    public List<MissionSummaryGetDto> getMissionList(@RequestParam("stampId") Long stampId) {
        return stampService.getMissions(stampId);
    }

    @StampUsersListGetApi
    @GetMapping("/users")
    public List<StampUsersGetDto> getStampUsers(@RequestParam("stampId") Long stampId) {
        return stampService.getStampUsers(stampId);
    }

    /** 스탬프 완료 기준 설정 */
    @PostMapping("/finishCnt")
    public void setFinishCnt(@RequestParam("stampId") Long stampId, @RequestParam("cnt") Integer cnt) {
        stampService.setFinishCnt(stampId, cnt);
    }
}
