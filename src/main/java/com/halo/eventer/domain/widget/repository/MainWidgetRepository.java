package com.halo.eventer.domain.widget.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.widget.entity.MainWidget;

public interface MainWidgetRepository extends JpaRepository<MainWidget, Long> {
    @Query("select e from MainWidget e WHERE e.festival.id = :festivalId")
    List<MainWidget> findAllByFestivalId(@Param("festivalId") Long festivalId);
}
