package com.halo.eventer.domain.manager.controller;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.manager.dto.ManagerCreateReqDto;
import com.halo.eventer.domain.manager.dto.ManagerListResDto;
import com.halo.eventer.domain.manager.service.ManagerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    /** 관리자 생성 */
    @PostMapping("/manager")
    public ManagerListResDto createManager(
            @RequestParam("festivalId") Long festivalId, @RequestBody ManagerCreateReqDto managerCreateReqDto) {
        managerService.createManager(festivalId, managerCreateReqDto);
        return new ManagerListResDto(managerService.getManagerList(festivalId));
    }

    /** 관리자 전체 조회 */
    @GetMapping("/manager")
    public ManagerListResDto getManagerList(@RequestParam("festivalId") Long festivalId) {
        return new ManagerListResDto(managerService.getManagerList(festivalId));
    }

    /** 관리자 삭제 */
    @DeleteMapping("/manager")
    public ManagerListResDto deleteManager(
            @RequestParam("festivalId") Long festivalId, @RequestParam("managerId") Long managerId) {
        managerService.deleteManager(managerId);
        return new ManagerListResDto(managerService.getManagerList(festivalId));
    }
}
