package com.halo.eventer.domain.lost_item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.lost_item.LostItem;

public interface LostItemRepository extends JpaRepository<LostItem, Long> {

    @Query("SELECT li FROM LostItem li WHERE li.festival.id = :festivalId")
    List<LostItem> findAllByFestivalId(@Param("festivalId") Long festivalId);
}
