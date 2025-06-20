package com.halo.eventer.domain.stamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.stamp.Stamp;

public interface StampRepository extends JpaRepository<Stamp, Long> {
    // TODO : 연관관계 있는 엔티티 자체로 조회해오는 것이 아닌 필드 값으로 조회해오는 방법 고려 가능
    List<Stamp> findByFestival(Festival festival);
}
