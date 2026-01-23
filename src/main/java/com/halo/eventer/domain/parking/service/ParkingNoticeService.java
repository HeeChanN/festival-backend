package com.halo.eventer.domain.parking.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.parking.ParkingManagement;
import com.halo.eventer.domain.parking.ParkingNotice;
import com.halo.eventer.domain.parking.dto.common.VisibilityChangeReqDto;
import com.halo.eventer.domain.parking.dto.parking_notice.ParkingNoticeContentReqDto;
import com.halo.eventer.domain.parking.dto.parking_notice.ParkingNoticeReqDto;
import com.halo.eventer.domain.parking.dto.parking_notice.ParkingNoticeResDto;
import com.halo.eventer.domain.parking.exception.ParkingManagementNotFoundException;
import com.halo.eventer.domain.parking.exception.ParkingNoticeNotFoundException;
import com.halo.eventer.domain.parking.repository.ParkingManagementRepository;
import com.halo.eventer.domain.parking.repository.ParkingNoticeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkingNoticeService {

    private final ParkingNoticeRepository parkingNoticeRepository;
    private final ParkingManagementRepository parkingManagementRepository;

    @Transactional
    public void create(final long parkingManagementId, ParkingNoticeReqDto parkingNoticeReqDto) {
        ParkingManagement parkingManagement = loadParkingManagementOrThrow(parkingManagementId);
        ParkingNotice parkingNotice =
                ParkingNotice.of(parkingNoticeReqDto.getTitle(), parkingNoticeReqDto.getContent(), parkingManagement);
        parkingNoticeRepository.save(parkingNotice);
    }

    @Transactional(readOnly = true)
    public List<ParkingNoticeResDto> getParkingNotices(final long parkingManagementId) {
        return parkingNoticeRepository.findByIdParkingManagementId(parkingManagementId).stream()
                .map(parkingNotice -> new ParkingNoticeResDto(
                        parkingNotice.getId(),
                        parkingNotice.getTitle(),
                        parkingNotice.getContent(),
                        parkingNotice.getVisible()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ParkingNoticeResDto> getVisibleParkingNotices(final long parkingManagementId) {
        return parkingNoticeRepository.findByIdParkingManagementIdAndVisible(parkingManagementId, true).stream()
                .map(parkingNotice -> new ParkingNoticeResDto(
                        parkingNotice.getId(),
                        parkingNotice.getTitle(),
                        parkingNotice.getContent(),
                        parkingNotice.getVisible()))
                .toList();
    }

    @Transactional
    public void updateContent(final long id, ParkingNoticeContentReqDto parkingNoticeContentReqDto) {
        ParkingNotice parkingNotice = loadParkingNoticeOrThrow(id);
        parkingNotice.updateNotice(parkingNoticeContentReqDto.getTitle(), parkingNoticeContentReqDto.getContent());
    }

    @Transactional
    public void changeVisibility(final long id, VisibilityChangeReqDto visibilityChangeReqDto) {
        ParkingNotice parkingNotice = loadParkingNoticeOrThrow(id);
        parkingNotice.changeVisible(visibilityChangeReqDto.getVisible());
    }

    @Transactional
    public void delete(final long id) {
        ParkingNotice parkingNotice = loadParkingNoticeOrThrow(id);
        parkingNoticeRepository.delete(parkingNotice);
    }

    private ParkingNotice loadParkingNoticeOrThrow(Long id) {
        return parkingNoticeRepository.findById(id).orElseThrow(() -> new ParkingNoticeNotFoundException(id));
    }

    private ParkingManagement loadParkingManagementOrThrow(Long id) {
        return parkingManagementRepository.findById(id).orElseThrow(() -> new ParkingManagementNotFoundException(id));
    }
}
