package com.halo.eventer.domain.widget_item.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.widget_item.WidgetItem;

public interface WidgetItemRepository extends JpaRepository<WidgetItem, Long> {

    @Query("SELECT wi FROM WidgetItem wi WHERE wi.baseWidget.id = :widgetId ")
    List<WidgetItem> findAllWidgetItemsByBaseWidgetId(@Param("widgetId") Long widgetId);

    @Query("SELECT wi FROM WidgetItem wi LEFT JOIN FETCH wi.images WHERE wi.id = :id ")
    Optional<WidgetItem> findWidgetItemById(@Param("id") Long id);
}
