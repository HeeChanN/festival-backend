package com.halo.eventer.domain.stamp.controller;

import java.util.List;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.stamp.dto.mission.response.MissionSummaryResDto;
import com.halo.eventer.domain.stamp.dto.stamp.*;
import com.halo.eventer.domain.stamp.service.StampService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stamp")
@Hidden
public class StampController {

    private final StampService stampService;

    @PostMapping
    public List<StampGetDto> registerStamp(@Min(1) @RequestParam("festivalId") long festivalId) {
        return stampService.registerStamp(festivalId);
    }

    @GetMapping
    public List<StampGetDto> getStampList(@Min(1) @RequestParam("festivalId") Long festivalId) {
        return stampService.getStampByFestivalId(festivalId);
    }

    @PatchMapping
    public void updateStampOn(@Min(1) @RequestParam("stampId") Long stampId) {
        stampService.updateStampOn(stampId);
    }

    @DeleteMapping
    public void deleteStamp(@Min(1) @RequestParam("stampId") Long stampId) {
        stampService.deleteStamp(stampId);
    }

    @GetMapping("/missions")
    public List<MissionSummaryResDto> getMissionList(@Min(1) @RequestParam("stampId") Long stampId) {
        return stampService.getMissions(stampId);
    }

    @GetMapping("/users")
    public List<StampUsersGetDto> getStampUsers(@Min(1) @RequestParam("stampId") Long stampId) {
        return stampService.getStampUsers(stampId);
    }

    @PostMapping("/finishCnt")
    public void setFinishCnt(@Min(1) @RequestParam("stampId") Long stampId, @Min(1) @RequestParam("cnt") int cnt) {
        stampService.setFinishCnt(stampId, cnt);
    }
}
