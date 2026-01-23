package com.halo.eventer.domain.parking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.parking.dto.common.VisibilityChangeReqDto;
import com.halo.eventer.domain.parking.dto.parking_lot.AdminParkingLotResDto;
import com.halo.eventer.domain.parking.dto.parking_lot.CongestionLevelReqDto;
import com.halo.eventer.domain.parking.dto.parking_lot.ParkingLotReqDto;
import com.halo.eventer.domain.parking.service.ParkingLotService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @PostMapping("/admin/parking-zones/{parkingZoneId}/parking-lots")
    public void create(
            @PathVariable("parkingZoneId") Long parkingZoneId, @RequestBody ParkingLotReqDto parkingLotReqDto) {
        parkingLotService.create(parkingZoneId, parkingLotReqDto);
    }

    @GetMapping("/admin/parking-lots/{id}")
    public AdminParkingLotResDto getParkingLot(@PathVariable("id") @Min(1) Long id) {
        return parkingLotService.getParkingLot(id);
    }

    @PatchMapping("/admin/parking-lots/{id}/visibility")
    public void changeVisible(@PathVariable("id") @Min(1) Long id, @RequestBody @Valid VisibilityChangeReqDto reqDto) {
        parkingLotService.changeVisible(id, reqDto);
    }

    @PutMapping("/admin/parking-lots/{id}")
    public void updateContent(@PathVariable("id") @Min(1) Long id, @RequestBody ParkingLotReqDto reqDto) {
        parkingLotService.updateContent(id, reqDto);
    }

    @PatchMapping("/admin/parking-lots/{id}/congestion-level")
    public void changeCongestionLevel(
            @PathVariable("id") @Min(1) Long id, @RequestBody @Valid CongestionLevelReqDto reqDto) {
        parkingLotService.changeCongestionLevel(id, reqDto);
    }

    @DeleteMapping("/admin/parking-lots/{id}")
    public void delete(@PathVariable("id") @Min(1) Long id) {
        parkingLotService.delete(id);
    }
}
