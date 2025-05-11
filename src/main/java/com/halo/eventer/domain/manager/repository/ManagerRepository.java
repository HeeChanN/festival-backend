package com.halo.eventer.domain.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.manager.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    @Query("select m from Manager m where m.festival.id = :id ")
    List<Manager> findManagerByFestivalId(@Param("id") Long id);
}
