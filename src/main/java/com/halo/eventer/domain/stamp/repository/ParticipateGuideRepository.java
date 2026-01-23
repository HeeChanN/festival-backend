package com.halo.eventer.domain.stamp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.stamp.ParticipateGuide;

public interface ParticipateGuideRepository extends JpaRepository<ParticipateGuide, Long> {
    @Query("select pg from ParticipateGuide pg where pg.stamp.id = :stampId")
    Optional<ParticipateGuide> findFirstByStampId(@Param("stampId") Long stampId);
}
