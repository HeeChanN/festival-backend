package com.halo.eventer.domain.widget.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.widget.BaseWidget;

public interface BaseWidgetRepository extends JpaRepository<BaseWidget, Long> {
    @Query("SELECT bw FROM BaseWidget bw "
            + "WHERE type(bw) = :childClass "
            + "AND bw.festival.id = :festivalId "
            + "ORDER BY bw.createdAt DESC")
    <T extends BaseWidget> Page<T> findChildCreateDesc(
            @Param("childClass") Class<T> childClass, @Param("festivalId") Long festivalId, Pageable pageable);

    @Query("SELECT bw FROM BaseWidget bw "
            + "WHERE type(bw) = :childClass "
            + "AND bw.festival.id = :festivalId "
            + "ORDER BY bw.updatedAt DESC")
    <T extends BaseWidget> Page<T> findChildUpdateDesc(
            @Param("childClass") Class<T> childClass, @Param("festivalId") Long festivalId, Pageable pageable);
}
