package com.halo.eventer.domain.stamp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.stamp.Mission;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    @Query("select m from Mission m where m.stamp.id = :stampId")
    List<Mission> findAllByStampId(@Param("stampId") Long stampId);

    @Query("select m from Mission m where m.id=:missionId and m.stamp.id=:stampId")
    Optional<Mission> findByIdAndStampId(@Param("missionId") Long missionId, @Param("stampId") Long stampId);
}
