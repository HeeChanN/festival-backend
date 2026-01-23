package com.halo.eventer.domain.parking.controller;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.parking.dto.common.VisibilityChangeReqDto;
import com.halo.eventer.domain.parking.dto.parking_notice.ParkingNoticeContentReqDto;
import com.halo.eventer.domain.parking.dto.parking_notice.ParkingNoticeReqDto;
import com.halo.eventer.domain.parking.dto.parking_notice.ParkingNoticeResDto;
import com.halo.eventer.domain.parking.service.ParkingNoticeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class ParkingNoticeController {

    private final ParkingNoticeService parkingNoticeService;

    @PostMapping("/admin/parking-managements/{parkingManagementId}/parking-notices")
    public void create(
            @Min(1) @PathVariable("parkingManagementId") Long parkingManagementId,
            @Valid @RequestBody ParkingNoticeReqDto parkingNoticeReqDto) {
        parkingNoticeService.create(parkingManagementId, parkingNoticeReqDto);
    }

    @GetMapping("/admin/parking-managements/{parkingManagementId}/parking-notices")
    public List<ParkingNoticeResDto> getParkingNotices(
            @Min(1) @PathVariable("parkingManagementId") Long parkingManagementId) {
        return parkingNoticeService.getParkingNotices(parkingManagementId);
    }

    @GetMapping("/user/parking-managements/{parkingManagementId}/parking-notices")
    public List<ParkingNoticeResDto> getParkingNotice(
            @Min(1) @PathVariable("parkingManagementId") Long parkingManagementId) {
        return parkingNoticeService.getVisibleParkingNotices(parkingManagementId);
    }

    @PatchMapping(("/admin/parking-notices/{id}/content"))
    public void updateContent(
            @Min(1) @PathVariable("id") Long id,
            @Valid @RequestBody ParkingNoticeContentReqDto parkingNoticeContentReqDto) {
        parkingNoticeService.updateContent(id, parkingNoticeContentReqDto);
    }

    @PatchMapping("/admin/parking-notices/{id}/visibility")
    public void changeVisibility(
            @Min(1) @PathVariable("id") Long id, @Valid @RequestBody VisibilityChangeReqDto visibilityChangeReqDto) {
        parkingNoticeService.changeVisibility(id, visibilityChangeReqDto);
    }

    @DeleteMapping("/admin/parking-notices/{id}")
    public void delete(@Min(1) @PathVariable("id") Long id) {
        parkingNoticeService.delete(id);
    }
}
