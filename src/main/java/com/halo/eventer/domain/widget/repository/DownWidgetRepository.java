package com.halo.eventer.domain.widget.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.widget.entity.DownWidget;

public interface DownWidgetRepository extends JpaRepository<DownWidget, Long> {

    @Query("select e from DownWidget e WHERE e.festival.id = :festivalId")
    List<DownWidget> findAllByFestivalId(@Param("festivalId") Long festivalId);
}
