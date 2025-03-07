package com.halo.eventer.domain.stamp.service;

import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.dto.mission.MissionDetailGetDto;
import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.exception.MissionNotFoundException;
import com.halo.eventer.domain.stamp.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MissionService {
  private final MissionRepository missionRepository;

  /** 미션 단일 조회 */
  public MissionDetailGetDto getMission(Long missionId) {
    Mission mission = missionRepository
            .findById(missionId)
            .orElseThrow(() -> new MissionNotFoundException(missionId));
    return MissionDetailGetDto.from(mission);
  }

  /** 미션 단일 수정 */
  @Transactional
  public void updateMission(Long missionId, MissionUpdateDto missionUpdateDto) {
    Mission mission = loadMissionOrThrow(missionId);
    mission.updateMission(missionUpdateDto);
  }

  private Mission loadMissionOrThrow(Long missionId) {
    return missionRepository
            .findById(missionId)
            .orElseThrow(() -> new MissionNotFoundException(missionId));
  }
}
