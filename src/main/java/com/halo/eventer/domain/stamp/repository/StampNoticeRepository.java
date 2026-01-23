package com.halo.eventer.domain.stamp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.stamp.StampNotice;

public interface StampNoticeRepository extends JpaRepository<StampNotice, Long> {

    @Query("select n from StampNotice n where n.stamp.id = :stampId")
    Optional<StampNotice> findByStampId(@Param("stampId") Long stampId);

    @Query("select n from StampNotice n where n.stamp.id = :stampId order by n.id desc")
    List<StampNotice> findAllByStampIdOrderByIdDesc(@Param("stampId") Long stampId);
}
