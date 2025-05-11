package com.halo.eventer.domain.inquiry.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.inquiry.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    @Query("SELECT i FROM Inquiry i WHERE i.festival.id = :festivalId ")
    List<Inquiry> findAllByFestivalId(@Param("festivalId") Long festivalId);

    @Query("SELECT i FROM Inquiry i WHERE i.festival.id = :festivalId")
    Page<Inquiry> findAllByFestivalId(@Param("festivalId") Long festivalId, Pageable pageable);

    @Query(
            value = "SELECT * FROM inquiry i "
                    + "WHERE i.festival_id = :festivalId "
                    + "AND (:lastId = 0 OR i.id < :lastId) "
                    + "ORDER BY i.id DESC "
                    + "LIMIT :size ",
            nativeQuery = true)
    List<Inquiry> getPageByFestivalIdAndLastId(
            @Param("festivalId") Long festivalId, @Param("lastId") Long lastId, @Param("size") int size);
}
