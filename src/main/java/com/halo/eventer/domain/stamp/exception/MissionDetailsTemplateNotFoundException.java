package com.halo.eventer.domain.stamp.exception;

import com.halo.eventer.global.error.exception.EntityNotFoundException;

public class MissionDetailsTemplateNotFoundException extends EntityNotFoundException {
    public MissionDetailsTemplateNotFoundException(Long id) {
        super(String.format("Mission Template with mission %d is not found", id));
    }
}
