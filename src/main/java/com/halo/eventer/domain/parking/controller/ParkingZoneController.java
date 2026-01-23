package com.halo.eventer.domain.parking.controller;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.*;

import com.halo.eventer.domain.parking.dto.common.DisplayOrderChangeReqDto;
import com.halo.eventer.domain.parking.dto.common.NameUpdateReqDto;
import com.halo.eventer.domain.parking.dto.common.VisibilityChangeReqDto;
import com.halo.eventer.domain.parking.dto.parking_zone.ParkingZoneReqDto;
import com.halo.eventer.domain.parking.dto.parking_zone.ParkingZoneResDto;
import com.halo.eventer.domain.parking.service.ParkingZoneService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class ParkingZoneController {

    private final ParkingZoneService parkingZoneService;

    @PostMapping("/admin/parking-managements/{parkingManagementId}/parking-zones")
    public void create(
            @PathVariable @Min(1) Long parkingManagementId, @RequestBody @Valid ParkingZoneReqDto parkingZoneReqDto) {
        parkingZoneService.create(parkingManagementId, parkingZoneReqDto);
    }

    @GetMapping("/admin/parking-managements/{parkingManagementId}/parking-zones")
    public List<ParkingZoneResDto> getParkingZones(@PathVariable @Min(1) Long parkingManagementId) {
        return parkingZoneService.getParkingZones(parkingManagementId);
    }

    @GetMapping("/user/parking-managements/{parkingManagementId}/parking-zones")
    public List<ParkingZoneResDto> getVisibleParkingZones(@PathVariable @Min(1) Long parkingManagementId) {
        return parkingZoneService.getVisibleParkingZones(parkingManagementId);
    }

    @PatchMapping("/admin/parking-zones/{id}/visibility")
    public void changeVisibility(
            @PathVariable("id") @Min(1) Long id, @RequestBody @Valid VisibilityChangeReqDto reqDto) {
        parkingZoneService.changeVisible(id, reqDto);
    }

    @PatchMapping("/admin/parking-zones/{id}/name")
    public void changeName(@PathVariable("id") @Min(1) Long id, @RequestBody @Valid NameUpdateReqDto reqDto) {
        parkingZoneService.changeName(id, reqDto);
    }

    @PutMapping("/admin/parking-managements/{parkingManagementId}/parking-zones/display-order")
    public void changeDisplayOrder(
            @PathVariable @Min(1) Long parkingManagementId, @RequestBody @Valid DisplayOrderChangeReqDto reqDto) {
        parkingZoneService.changeDisplayOrder(parkingManagementId, reqDto);
    }

    @DeleteMapping("/admin/parking-managements/{parkingManagementId}/parking-zones/{id}")
    public void delete(@PathVariable @Min(1) Long parkingManagementId, @PathVariable("id") @Min(1) Long id) {
        parkingZoneService.delete(parkingManagementId, id);
    }
}
