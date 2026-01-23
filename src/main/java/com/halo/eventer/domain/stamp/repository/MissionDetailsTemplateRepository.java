package com.halo.eventer.domain.stamp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.stamp.MissionDetailsTemplate;

public interface MissionDetailsTemplateRepository extends JpaRepository<MissionDetailsTemplate, Long> {
    @Query("select m from MissionDetailsTemplate m where m.mission.id = :missionId")
    Optional<MissionDetailsTemplate> findByMissionId(@Param("missionId") Long missionId);
}
