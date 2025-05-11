package com.halo.eventer.domain.missing_person.repository;

import java.util.List;
import javax.swing.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.missing_person.MissingPerson;

public interface MissingPersonRepository extends JpaRepository<MissingPerson, Long> {

    @Query("SELECT m FROM MissingPerson m " + "WHERE m.festival.id = :festivalId " + "AND m.popup = :popup ")
    List<MissingPerson> findAllByFestivalIdAndPopup(
            @Param("festivalId") Long festivalId, @Param("popup") Boolean popup);
}
