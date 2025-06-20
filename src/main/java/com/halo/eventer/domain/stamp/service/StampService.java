package com.halo.eventer.domain.stamp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.dto.stamp.*;
import com.halo.eventer.domain.stamp.exception.StampNotFoundException;
import com.halo.eventer.domain.stamp.repository.MissionRepository;
import com.halo.eventer.domain.stamp.repository.StampRepository;
import com.halo.eventer.global.utils.EncryptService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StampService {

    private final StampRepository stampRepository;
    private final FestivalRepository festivalRepository;
    private final EncryptService encryptService;
    private final MissionRepository missionRepository;

    @Transactional
    public List<StampGetDto> registerStamp(Long festivalId) {
        Festival festival = loadFestivalOrThrow(festivalId);
        stampRepository.save(Stamp.create(festival));
        List<Stamp> stamps = stampRepository.findByFestival(festival);
        return StampGetDto.fromEntities(stamps);
    }

    @Transactional(readOnly = true)
    public List<StampGetDto> getStampByFestivalId(Long festivalId) {
        Festival festival = loadFestivalOrThrow(festivalId);
        List<Stamp> stamps = stampRepository.findByFestival(festival);
        return StampGetDto.fromEntities(stamps);
    }

    @Transactional
    public void updateStampOn(Long stampId) {
        Stamp stamp = loadStampOrThrow(stampId);
        stamp.switchActivation();
    }

    @Transactional
    public void deleteStamp(Long stampId) {
        Stamp stamp = loadStampOrThrow(stampId);
        stampRepository.delete(stamp);
    }

    @Transactional
    public void createMission(Long stampId, MissionSetListDto dto) {
        Stamp stamp = loadStampOrThrow(stampId);
        stamp.validateActivation();
        List<Mission> missions = createMissionsFromDto(dto, stamp);
        missionRepository.saveAll(missions);
    }

    @Transactional(readOnly = true)
    public List<MissionSummaryGetDto> getMissions(Long stampId) {
        Stamp stamp = loadStampOrThrow(stampId);
        return MissionSummaryGetDto.fromEntities(stamp.getMissions());
    }

    @Transactional(readOnly = true)
    public List<StampUsersGetDto> getStampUsers(Long stampId) {
        Stamp stamp = loadStampOrThrow(stampId);
        return stamp.getStampUsers().stream()
                .map(user -> new StampUsersGetDto(
                        user.getUuid(),
                        encryptService.decryptInfo(user.getName()),
                        encryptService.decryptInfo(user.getPhone()),
                        user.isFinished(),
                        user.getParticipantCount()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void setFinishCnt(Long stampId, int cnt) {
        Stamp stamp = loadStampOrThrow(stampId);
        stamp.defineFinishCnt(cnt);
    }

    private Festival loadFestivalOrThrow(Long id) {
        return festivalRepository.findById(id).orElseThrow(() -> new FestivalNotFoundException(id));
    }

    private Stamp loadStampOrThrow(Long stampId) {
        return stampRepository.findById(stampId).orElseThrow(() -> new StampNotFoundException(stampId));
    }

    private List<Mission> createMissionsFromDto(MissionSetListDto dto, Stamp stamp) {
        return dto.getMissionSets().stream()
                .map(missionDto -> {
                    Mission mission = Mission.from(missionDto);
                    mission.addStamp(stamp);
                    return mission;
                })
                .collect(Collectors.toList());
    }
}
