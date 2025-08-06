package com.halo.eventer.domain.stamp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.stamp.dto.stamp.*;
import com.halo.eventer.domain.stamp.service.StampService;
import lombok.RequiredArgsConstructor;

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

    @PatchMapping
    public void updateStampOn(@RequestParam("stampId") Long stampId) {
        stampService.updateStampOn(stampId);
    }

    @DeleteMapping
    public void deleteStamp(@RequestParam("stampId") Long stampId) {
        stampService.deleteStamp(stampId);
    }

    @GetMapping("/missions")
    public List<MissionSummaryGetDto> getMissionList(@RequestParam("stampId") Long stampId) {
        return stampService.getMissions(stampId);
    }

    @GetMapping("/users")
    public List<StampUsersGetDto> getStampUsers(@RequestParam("stampId") Long stampId) {
        return stampService.getStampUsers(stampId);
    }

    @PostMapping("/finishCnt")
    public void setFinishCnt(@RequestParam("stampId") Long stampId, @RequestParam("cnt") int cnt) {
        stampService.setFinishCnt(stampId, cnt);
    }
}
