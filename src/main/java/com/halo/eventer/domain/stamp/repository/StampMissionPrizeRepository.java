package com.halo.eventer.domain.stamp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.stamp.StampMissionPrize;

public interface StampMissionPrizeRepository extends JpaRepository<StampMissionPrize, Long> {
    List<StampMissionPrize> findAllByStampIdOrderByRequiredCountAsc(Long stampId);

    @Query("select p from StampMissionPrize p where p.id = :prizeId and p.stamp.id = :stampId")
    Optional<StampMissionPrize> findByIdAndStampId(@Param("prizeId") Long prizeId, @Param("stampId") Long stampId);
}
