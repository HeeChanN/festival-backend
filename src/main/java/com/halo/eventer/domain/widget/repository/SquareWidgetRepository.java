package com.halo.eventer.domain.widget.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.widget.entity.SquareWidget;

public interface SquareWidgetRepository extends JpaRepository<SquareWidget, Long> {

    @Query("SELECT sw FROM SquareWidget sw WHERE sw.festival.id = :festivalId ")
    List<SquareWidget> findAllByFestivalId(@Param("festivalId") Long festivalId);
}
