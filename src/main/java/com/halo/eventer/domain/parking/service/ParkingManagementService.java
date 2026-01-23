package com.halo.eventer.domain.parking.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.image.dto.FileDto;
import com.halo.eventer.domain.parking.ParkingManagement;
import com.halo.eventer.domain.parking.dto.common.DisplayOrderChangeReqDto;
import com.halo.eventer.domain.parking.dto.parking_management.*;
import com.halo.eventer.domain.parking.enums.ParkingInfoType;
import com.halo.eventer.domain.parking.exception.ParkingManagementNotFoundException;
import com.halo.eventer.domain.parking.repository.ParkingManagementRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkingManagementService {

    private final ParkingManagementRepository parkingManagementRepository;
    private final FestivalRepository festivalRepository;

    @Transactional
    public void create(Long festivalId, ParkingManagementReqDto parkingManagementReqDto) {
        Festival festival =
                festivalRepository.findById(festivalId).orElseThrow(() -> new FestivalNotFoundException(festivalId));

        ParkingManagement parkingManagement = ParkingManagement.of(
                festival,
                parkingManagementReqDto.getHeaderName(),
                ParkingInfoType.valueOf(parkingManagementReqDto.getParkingInfoType()),
                parkingManagementReqDto.getTitle(),
                parkingManagementReqDto.getDescription(),
                parkingManagementReqDto.getButtonName(),
                parkingManagementReqDto.getButtonTargetUrl(),
                parkingManagementReqDto.getBackgroundImage(),
                parkingManagementReqDto.isVisible());
        parkingManagementRepository.save(parkingManagement);
    }

    @Transactional(readOnly = true)
    public ParkingManagementResDto getParkingManagement(Long id) {
        ParkingManagement parkingManagement =
                parkingManagementRepository.findById(id).orElseThrow(() -> new ParkingManagementNotFoundException(id));
        return ParkingManagementResDto.of(
                parkingManagement.getId(),
                parkingManagement.getHeaderName(),
                parkingManagement.getParkingInfoType().name(),
                parkingManagement.getTitle(),
                parkingManagement.getDescription(),
                parkingManagement.getButtonName(),
                parkingManagement.getButtonTargetUrl(),
                parkingManagement.getBackgroundImage(),
                parkingManagement.isVisible());
    }

    @Transactional(readOnly = true)
    public ParkingManagementResDto getVisibleParkingManagement(Long id) {
        ParkingManagement parkingManagement = parkingManagementRepository
                .findByIdAndVisible(id, true)
                .orElseThrow(() -> new ParkingManagementNotFoundException(id));
        return ParkingManagementResDto.of(
                parkingManagement.getId(),
                parkingManagement.getHeaderName(),
                parkingManagement.getParkingInfoType().name(),
                parkingManagement.getTitle(),
                parkingManagement.getDescription(),
                parkingManagement.getButtonName(),
                parkingManagement.getButtonTargetUrl(),
                parkingManagement.getBackgroundImage(),
                parkingManagement.isVisible());
    }

    @Transactional
    public void update(Long id, ParkingManagementReqDto parkingManagementReqDto) {
        ParkingManagement parkingManagement = loadParkingManagementOrThrow(id);
        parkingManagement.update(
                parkingManagementReqDto.getHeaderName(),
                ParkingInfoType.valueOf(parkingManagementReqDto.getParkingInfoType()),
                parkingManagementReqDto.getTitle(),
                parkingManagementReqDto.getDescription(),
                parkingManagementReqDto.getButtonName(),
                parkingManagementReqDto.getButtonTargetUrl(),
                parkingManagementReqDto.getBackgroundImage(),
                parkingManagementReqDto.isVisible());
    }

    @Transactional(readOnly = true)
    public ParkingManagementSubPageResDto getParkingManagementSubPage(Long id) {
        ParkingManagement parkingManagement = parkingManagementRepository
                .findByIdWithImages(id)
                .orElseThrow(() -> new ParkingManagementNotFoundException(id));
        return new ParkingManagementSubPageResDto(
                parkingManagement.getSubPageHeaderName(), parkingManagement.getImages());
    }

    @Transactional
    public void updateSubPageHeaderName(Long id, ParkingSubPageReqDto parkingSubPageReqDto) {
        ParkingManagement parkingManagement = loadParkingManagementOrThrow(id);
        parkingManagement.updateSubPageHeaderName(parkingSubPageReqDto.getSubPageHeaderName());
    }

    @Transactional
    public void createParkingMapImage(Long id, FileDto fileDto) {
        ParkingManagement parkingManagement = loadParkingManagementOrThrow(id);
        parkingManagement.addImage(fileDto.getUrl());
    }

    @Transactional
    public void updateParkingMapImageDisplayOrder(Long id, DisplayOrderChangeReqDto displayOrderChangeReqDto) {
        ParkingManagement parkingManagement = parkingManagementRepository
                .findByIdWithImages(id)
                .orElseThrow(() -> new ParkingManagementNotFoundException(id));

        parkingManagement.reorderImages(displayOrderChangeReqDto.getIds());
    }

    @Transactional
    public void deleteParkingMapImages(Long id, DisplayOrderChangeReqDto displayOrderChangeReqDto) {
        ParkingManagement parkingManagement = parkingManagementRepository
                .findByIdWithImages(id)
                .orElseThrow(() -> new ParkingManagementNotFoundException(id));
        parkingManagement.removeImages(displayOrderChangeReqDto.getIds());
    }

    @Transactional(readOnly = true)
    public ParkingManagementIdResDto getParkingManagementId(Long festivalId) {
        List<ParkingManagement> parkingManagements = parkingManagementRepository.findByFestivalId(festivalId);
        return parkingManagements.isEmpty()
                ? null
                : new ParkingManagementIdResDto(parkingManagements.get(0).getId());
    }

    private ParkingManagement loadParkingManagementOrThrow(Long id) {
        return parkingManagementRepository.findById(id).orElseThrow(() -> new ParkingManagementNotFoundException(id));
    }
}
