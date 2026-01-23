package com.halo.eventer.domain.stamp.exception;

import com.halo.eventer.global.error.exception.EntityNotFoundException;

public class MissionPrizeNotFoundException extends EntityNotFoundException {
    public MissionPrizeNotFoundException(Long id) {
        super(String.format("Mission Prize with %d is not found", id));
    }
}
