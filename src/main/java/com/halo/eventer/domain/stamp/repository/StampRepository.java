package com.halo.eventer.domain.stamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.stamp.Stamp;

public interface StampRepository extends JpaRepository<Stamp, Long> {
    List<Stamp> findByFestival(Festival festival);
}
