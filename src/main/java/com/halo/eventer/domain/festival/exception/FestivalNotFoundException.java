package com.halo.eventer.domain.festival.exception;

import com.halo.eventer.global.error.exception.EntityNotFoundException;

public class FestivalNotFoundException extends EntityNotFoundException {
    public FestivalNotFoundException(Long id) {
        super(String.format("Festival with %d is not found", id));
    }

    public FestivalNotFoundException(String subAddress) {
        super(String.format("Festival with %s is not found", subAddress));
    }
}
