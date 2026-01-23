package com.halo.eventer.domain.stamp.exception;

import com.halo.eventer.global.error.exception.EntityNotFoundException;

public class ParticipateGuidePageNotFoundException extends EntityNotFoundException {
    public ParticipateGuidePageNotFoundException(Long id) {
        super(String.format("Participation Guide Page with %d is not found", id));
    }
}
