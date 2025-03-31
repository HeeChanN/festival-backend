package com.halo.eventer.domain.map.repository;

import com.halo.eventer.domain.map.Map;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MapRepository extends JpaRepository<Map, Long> {

    @Query("SELECT m FROM Map m " +
            "LEFT JOIN FETCH m.mapCategory " +
            "LEFT JOIN FETCH m.durationMaps dm " +
            "LEFT JOIN FETCH dm.duration " +
            "WHERE m.id = :mapId ")
    Optional<Map> findByIdWithCategoryAndDuration(@Param("mapId") Long mapId);

    @Query("SELECT DISTINCT m FROM Map m " +
            "LEFT JOIN FETCH m.mapCategory " +
            "LEFT JOIN FETCH m.durationMaps dm " +
            "LEFT JOIN FETCH dm.duration " +
            "WHERE m.mapCategory.id= :mapCategoryId ")
    List<Map> findByCategoryIdWithCategoryAndDuration(@Param("mapCategoryId") Long mapCategoryId);
}
