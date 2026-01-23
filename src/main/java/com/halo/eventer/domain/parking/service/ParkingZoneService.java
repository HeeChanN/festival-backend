package com.halo.eventer.domain.parking.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.parking.ParkingManagement;
import com.halo.eventer.domain.parking.ParkingZone;
import com.halo.eventer.domain.parking.dto.common.DisplayOrderChangeReqDto;
import com.halo.eventer.domain.parking.dto.common.NameUpdateReqDto;
import com.halo.eventer.domain.parking.dto.common.VisibilityChangeReqDto;
import com.halo.eventer.domain.parking.dto.parking_lot.ParkingLotSummaryDto;
import com.halo.eventer.domain.parking.dto.parking_zone.ParkingZoneReqDto;
import com.halo.eventer.domain.parking.dto.parking_zone.ParkingZoneResDto;
import com.halo.eventer.domain.parking.exception.ParkingManagementNotFoundException;
import com.halo.eventer.domain.parking.exception.ParkingZoneNotFoundException;
import com.halo.eventer.domain.parking.repository.ParkingManagementRepository;
import com.halo.eventer.domain.parking.repository.ParkingZoneRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkingZoneService {

    private final ParkingZoneRepository parkingZoneRepository;
    private final ParkingManagementRepository parkingManagementRepository;

    @Transactional
    public void create(final long parkingManagementId, ParkingZoneReqDto parkingZoneReqDto) {
        ParkingManagement parkingManagement = parkingManagementRepository
                .findByIdWithParkingZone(parkingManagementId)
                .orElseThrow(() -> new ParkingManagementNotFoundException(parkingManagementId));
        ParkingZone parkingZone =
                ParkingZone.of(parkingZoneReqDto.getName(), parkingZoneReqDto.getVisible(), parkingManagement);
    }

    @Transactional(readOnly = true)
    public List<ParkingZoneResDto> getParkingZones(final long parkingManagementId) {
        return parkingZoneRepository.findAllByParkingManagementId(parkingManagementId).stream()
                .map(parkingZone -> new ParkingZoneResDto(
                        parkingZone.getId(),
                        parkingZone.getName(),
                        parkingZone.getVisible(),
                        parkingZone.getParkingLots().stream()
                                .map(parkingLot -> new ParkingLotSummaryDto(
                                        parkingLot.getId(),
                                        parkingLot.getName(),
                                        parkingLot.getCongestionLevel().toString(),
                                        parkingLot.getVisible(),
                                        parkingLot.getCopyAddress(),
                                        parkingLot.getDisplayAddress(),
                                        parkingLot.getLatitude(),
                                        parkingLot.getLongitude()))
                                .toList()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ParkingZoneResDto> getVisibleParkingZones(final long parkingManagementId) {
        return parkingZoneRepository.findAllByParkingManagementIdAndVisible(parkingManagementId, true).stream()
                .map(parkingZone -> new ParkingZoneResDto(
                        parkingZone.getId(),
                        parkingZone.getName(),
                        parkingZone.getVisible(),
                        parkingZone.getParkingLots().stream()
                                .filter(parkingLot -> Boolean.TRUE.equals(parkingLot.getVisible()))
                                .map(parkingLot -> new ParkingLotSummaryDto(
                                        parkingLot.getId(),
                                        parkingLot.getName(),
                                        parkingLot.getCongestionLevel().toString(),
                                        parkingLot.getVisible(),
                                        parkingLot.getCopyAddress(),
                                        parkingLot.getDisplayAddress(),
                                        parkingLot.getLatitude(),
                                        parkingLot.getLongitude()))
                                .toList()))
                .toList();
    }

    @Transactional
    public void changeVisible(final long id, VisibilityChangeReqDto parkingZoneReqDto) {
        ParkingZone parkingZone = loadParkingZoneOrThrow(id);
        parkingZone.changeVisible(parkingZoneReqDto.getVisible());
    }

    @Transactional
    public void changeName(final long id, NameUpdateReqDto nameUpdateReqDto) {
        ParkingZone parkingZone = loadParkingZoneOrThrow(id);
        parkingZone.updateName(nameUpdateReqDto.getName());
    }

    @Transactional
    public void changeAllVisible(final long id, VisibilityChangeReqDto visibilityChangeReqDto) {
        ParkingZone parkingZone = parkingZoneRepository
                .findByIdWithParkingLot(id)
                .orElseThrow(() -> new ParkingZoneNotFoundException(id));
        parkingZone.changeAllVisible(visibilityChangeReqDto.getVisible());
    }

    @Transactional
    public void changeDisplayOrder(final long parkingManagementId, DisplayOrderChangeReqDto displayOrderChangeReqDto) {
        ParkingManagement parkingManagement = parkingManagementRepository
                .findByIdWithParkingZone(parkingManagementId)
                .orElseThrow(() -> new ParkingManagementNotFoundException(parkingManagementId));
        parkingManagement.reorderParkingZones(displayOrderChangeReqDto.getIds());
    }

    @Transactional
    public void delete(final long parkingManagementId, final long id) {
        ParkingManagement parkingManagement = parkingManagementRepository
                .findByIdWithParkingZone(parkingManagementId)
                .orElseThrow(() -> new ParkingManagementNotFoundException(parkingManagementId));
        parkingManagement.removeParkingZone(id);
    }

    private ParkingZone loadParkingZoneOrThrow(Long id) {
        return parkingZoneRepository.findById(id).orElseThrow(() -> new ParkingZoneNotFoundException(id));
    }
}
