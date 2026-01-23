package com.halo.eventer.domain.stamp.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.stamp.*;

public interface MissionExtraInfoRepository extends JpaRepository<MissionExtraInfo, Long> {
    @Query("select e from MissionExtraInfo e where e.template.id = :templateId")
    List<MissionExtraInfo> findAllByTemplateId(@Param("templateId") Long templateId);
}
