package com.halo.eventer.domain.duration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.duration.Duration;

public interface DurationRepository extends CrudRepository<Duration, Long> {

    @Query("SELECT d FROM Duration d where d.festival.id = :festivalId")
    List<Duration> findAllByFestivalId(@Param("festivalId") Long festivalId);

    List<Duration> findAllByIdIn(List<Long> ids);
}
