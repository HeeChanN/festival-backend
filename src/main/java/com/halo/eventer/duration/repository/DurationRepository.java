package com.halo.eventer.duration.repository;

import com.halo.eventer.duration.Duration;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DurationRepository extends CrudRepository<Duration, Long> {

    List<Duration> findAllByFestivalId(Long id);

    List<Duration> findByIdIn(List<Long> ids);

}
