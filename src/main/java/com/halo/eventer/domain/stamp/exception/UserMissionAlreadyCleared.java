package com.halo.eventer.domain.stamp.exception;

import com.halo.eventer.global.error.exception.ForbiddenException;

public class UserMissionAlreadyCleared extends ForbiddenException {
    public UserMissionAlreadyCleared(Long id) {
        super(String.format("Mission with %d is already Cleared", id));
    }
}
