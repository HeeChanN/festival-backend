package com.halo.eventer.domain.stamp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.stamp.PageTemplate;
import com.halo.eventer.domain.stamp.dto.stamp.enums.PageType;

public interface PageTemplateRepository extends JpaRepository<PageTemplate, Long> {
    @Query(
            """
           select pt
           from PageTemplate pt
           where pt.stamp.id = :stampId
             and pt.type = :type
           """)
    Optional<PageTemplate> findFirstByStampIdAndType(@Param("stampId") Long stampId, @Param("type") PageType type);
}
