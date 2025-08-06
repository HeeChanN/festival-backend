package com.halo.eventer.domain.stamp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.dto.mission.MissionDetailGetDto;
import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.dto.stamp.MissionSetDto;
import com.halo.eventer.domain.stamp.dto.stamp.MissionSummaryGetDto;
import com.halo.eventer.domain.stamp.exception.MissionNotFoundException;
import com.halo.eventer.domain.stamp.exception.StampNotFoundException;
import com.halo.eventer.domain.stamp.repository.MissionRepository;
import com.halo.eventer.domain.stamp.repository.StampRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final StampRepository stampRepository;

    @Transactional
    public void createMission(Long stampId, List<MissionSetDto> dto) {
        Stamp stamp = loadStampOrThrow(stampId);
        stamp.validateActivation();
        List<Mission> missions = createMissionsFromDto(dto, stamp);
        missionRepository.saveAll(missions);
    }

    @Transactional(readOnly = true)
    public MissionDetailGetDto getMission(Long missionId) {
        Mission mission = loadMissionOrThrow(missionId);
        return MissionDetailGetDto.from(mission);
    }

    @Transactional(readOnly = true)
    public List<MissionSummaryGetDto> getMissions(Long stampId) {
        Stamp stamp = loadStampOrThrow(stampId);
        return MissionSummaryGetDto.fromEntities(stamp.getMissions());
    }

    @Transactional
    public void updateMission(Long missionId, MissionUpdateDto missionUpdateDto) {
        Mission mission = loadMissionOrThrow(missionId);
        mission.updateMission(missionUpdateDto);
    }

    private List<Mission> createMissionsFromDto(List<MissionSetDto> dto, Stamp stamp) {
        return dto.stream()
                .map(missionDto -> {
                    Mission mission = Mission.from(missionDto);
                    mission.addStamp(stamp);
                    return mission;
                })
                .collect(Collectors.toList());
    }

    private Mission loadMissionOrThrow(Long missionId) {
        return missionRepository.findById(missionId).orElseThrow(() -> new MissionNotFoundException(missionId));
    }

    private Stamp loadStampOrThrow(Long stampId) {
        return stampRepository.findById(stampId).orElseThrow(() -> new StampNotFoundException(stampId));
    }
}
