package com.halo.eventer.domain.festival.repository;

import com.halo.eventer.domain.festival.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FestivalRepository extends JpaRepository<Festival,Long> {

    Optional<Festival> findByName(String name);
    Optional<Festival> findBySubAddress(String subAddress);

    @Query("SELECT f FROM Festival f join FETCH f.missingPersons m where f.id = :id and m.popup= :status ")
    Optional<Festival> findMissingPersonWidgetById(Long id, Boolean status);
}
