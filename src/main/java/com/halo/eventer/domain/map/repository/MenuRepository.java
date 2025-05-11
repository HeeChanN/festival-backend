package com.halo.eventer.domain.map.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.map.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByIdIn(List<Long> ids);

    @Query("SELECT m FROM Menu m WHERE m.map.id = :mapId ")
    List<Menu> findAllByMapId(@Param("mapId") Long mapId);
}
