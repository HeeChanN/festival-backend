package com.halo.eventer.domain.widget.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.WidgetType;

public interface WidgetRepository extends JpaRepository<Widget, Long> {

    List<Widget> findAllByFestivalIdAndWidgetType(Long festivalId, WidgetType widgetType);

    @Query("SELECT w FROM Widget w "
            + "WHERE w.festival.id = :festivalId "
            + "AND w.widgetType = :widgetType "
            + "ORDER BY w.createdAt DESC")
    Page<Widget> findByFestivalIdAndWidgetTypeOrderByCreatedAtDesc(
            @Param("festivalId") Long festivalId, @Param("widgetType") WidgetType widgetType, Pageable pageable);

    @Query("SELECT w FROM Widget w "
            + "WHERE w.festival.id = :festivalId "
            + "AND w.widgetType = :widgetType "
            + "ORDER BY w.updatedAt DESC")
    Page<Widget> findByFestivalIdAndWidgetTypeOrderByUpdatedAtDesc(
            @Param("festivalId") Long festivalId, @Param("widgetType") WidgetType widgetType, Pageable pageable);

    @Query(
            value = "SELECT * FROM widget w WHERE w.festival_id = :festivalId "
                    + "AND w.widget_type = 'UP' "
                    + "AND :now BETWEEN "
                    + "  CAST(JSON_UNQUOTE(JSON_EXTRACT(w.properties, '$.periodStart')) AS DATETIME) "
                    + "  AND CAST(JSON_UNQUOTE(JSON_EXTRACT(w.properties, '$.periodEnd')) AS DATETIME)",
            nativeQuery = true)
    List<Widget> findUpWidgetsByFestivalIdAndPeriod(
            @Param("festivalId") Long festivalId, @Param("now") LocalDateTime now);
}
