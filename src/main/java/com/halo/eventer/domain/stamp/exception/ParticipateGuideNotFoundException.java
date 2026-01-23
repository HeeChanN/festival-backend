package com.halo.eventer.domain.stamp.exception;

import com.halo.eventer.global.error.exception.EntityNotFoundException;

public class ParticipateGuideNotFoundException extends EntityNotFoundException {
    public ParticipateGuideNotFoundException(Long id) {
        super(String.format("Participation Guide with %d is not found", id));
    }
}
