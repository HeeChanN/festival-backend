package com.halo.eventer.domain.stamp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.dto.mission.MissionDetailGetDto;
import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.exception.MissionNotFoundException;
import com.halo.eventer.domain.stamp.repository.MissionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MissionService {
    private final MissionRepository missionRepository;

    @Transactional(readOnly = true)
    public MissionDetailGetDto getMission(Long missionId) {
        Mission mission = loadMissionOrThrow(missionId);
        return MissionDetailGetDto.from(mission);
    }

    @Transactional
    public void updateMission(Long missionId, MissionUpdateDto missionUpdateDto) {
        Mission mission = loadMissionOrThrow(missionId);
        mission.updateMission(missionUpdateDto);
    }

    private Mission loadMissionOrThrow(Long missionId) {
        return missionRepository.findById(missionId).orElseThrow(() -> new MissionNotFoundException(missionId));
    }
}
