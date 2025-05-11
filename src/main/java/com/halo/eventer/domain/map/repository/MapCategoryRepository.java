package com.halo.eventer.domain.map.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.map.MapCategory;

public interface MapCategoryRepository extends JpaRepository<MapCategory, Long> {

    @Query("SELECT mc FROM MapCategory mc WHERE mc.festival.id = :festivalId ")
    List<MapCategory> findAllByFestivalId(@Param("festivalId") Long festivalId);
}
