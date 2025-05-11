package com.halo.eventer.domain.image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteByIdIn(List<Long> ids);
}
