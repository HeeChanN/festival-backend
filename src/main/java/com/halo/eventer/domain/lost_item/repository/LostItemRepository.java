package com.halo.eventer.domain.lost_item.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.halo.eventer.domain.lost_item.LostItem;

public interface LostItemRepository extends JpaRepository<LostItem, Long> {}
