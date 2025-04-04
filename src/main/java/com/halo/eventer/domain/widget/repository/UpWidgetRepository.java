package com.halo.eventer.domain.widget.repository;

import com.halo.eventer.domain.widget.entity.UpWidget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UpWidgetRepository extends JpaRepository<UpWidget, Long> {

    @Query("SELECT u FROM UpWidget u " +
            "WHERE u.festival.id = :festivalId " +
            "ORDER BY u.createdAt DESC")
    Page<UpWidget> findByFestivalIdOrderByCreateAtDesc(@Param("festivalId") Long festivalId, Pageable pageable);

    @Query("SELECT u FROM UpWidget u " +
            "WHERE u.festival.id = :festivalId " +
            "ORDER BY u.updatedAt DESC")
    Page<UpWidget> findByFestivalIdOrderByUpdateAtDesc(@Param("festivalId") Long festivalId, Pageable pageable);

    @Query("SELECT u FROM UpWidget u " +
            "WHERE u.festival.id = :festivalId " +
            "AND :now BETWEEN u.periodFeature.periodStart AND u.periodFeature.periodEnd ")
    List<UpWidget> findAllByFestivalIdAndPeriod(
            @Param("festivalId") Long festivalId,
            @Param("now") LocalDateTime now
    );
}
