package com.halo.eventer.domain.parking.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.parking.ParkingLot;
import com.halo.eventer.domain.parking.ParkingZone;
import com.halo.eventer.domain.parking.dto.common.VisibilityChangeReqDto;
import com.halo.eventer.domain.parking.dto.parking_lot.AdminParkingLotResDto;
import com.halo.eventer.domain.parking.dto.parking_lot.CongestionLevelReqDto;
import com.halo.eventer.domain.parking.dto.parking_lot.ParkingLotReqDto;
import com.halo.eventer.domain.parking.exception.ParkingZoneNotFoundException;
import com.halo.eventer.domain.parking.repository.ParkingLotRepository;
import com.halo.eventer.domain.parking.repository.ParkingZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingZoneRepository parkingZoneRepository;

    @Transactional
    public void create(final long parkingZoneId, ParkingLotReqDto parkingLotReqDto) {
        ParkingZone parkingZone = parkingZoneRepository
                .findById(parkingZoneId)
                .orElseThrow(() -> new ParkingZoneNotFoundException(parkingZoneId));
        ParkingLot parkingLot = ParkingLot.of(
                parkingLotReqDto.getName(),
                parkingLotReqDto.getSido(),
                parkingLotReqDto.getSigungu(),
                parkingLotReqDto.getDongmyun(),
                parkingLotReqDto.getRoadName(),
                parkingLotReqDto.getRoadNumber(),
                parkingLotReqDto.getBuildingName(),
                parkingLotReqDto.getLatitude(),
                parkingLotReqDto.getLongitude(),
                parkingZone);
    }

    @Transactional(readOnly = true)
    public AdminParkingLotResDto getParkingLot(final long id) {
        ParkingLot parkingLot = loadParkingLotOrThrow(id);
        return new AdminParkingLotResDto(
                parkingLot.getId(),
                parkingLot.getName(),
                parkingLot.getSido(),
                parkingLot.getSigungu(),
                parkingLot.getDongmyun(),
                parkingLot.getRoadName(),
                parkingLot.getRoadNumber(),
                parkingLot.getBuildingName(),
                parkingLot.getLatitude(),
                parkingLot.getLongitude());
    }

    @Transactional
    public void changeVisible(final long id, VisibilityChangeReqDto visibilityChangeReqDto) {
        ParkingLot parkingLot = loadParkingLotOrThrow(id);
        parkingLot.changeVisible(visibilityChangeReqDto.getVisible());
    }

    @Transactional
    public void updateContent(final long id, ParkingLotReqDto parkingLotReqDto) {
        ParkingLot parkingLot = loadParkingLotOrThrow(id);
        parkingLot.updateContent(
                parkingLotReqDto.getName(),
                parkingLotReqDto.getSido(),
                parkingLotReqDto.getSigungu(),
                parkingLotReqDto.getDongmyun(),
                parkingLotReqDto.getRoadName(),
                parkingLotReqDto.getRoadNumber(),
                parkingLotReqDto.getBuildingName(),
                parkingLotReqDto.getLatitude(),
                parkingLotReqDto.getLongitude());
    }

    @Transactional
    public void changeCongestionLevel(final long id, CongestionLevelReqDto congestionLevelReqDto) {
        ParkingLot parkingLot = loadParkingLotOrThrow(id);
        parkingLot.changeCongestionLevel(congestionLevelReqDto.getCongestionLevel());
    }

    @Transactional
    public void delete(final long id) {
        ParkingLot parkingLot = loadParkingLotOrThrow(id);
        parkingLotRepository.delete(parkingLot);
    }

    private ParkingLot loadParkingLotOrThrow(Long id) {
        return parkingLotRepository.findById(id).orElseThrow(() -> new ParkingZoneNotFoundException(id));
    }
}
