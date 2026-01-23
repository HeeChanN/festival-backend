package com.halo.eventer.domain.stamp.exception;

import com.halo.eventer.global.error.exception.EntityNotFoundException;

public class PageTemplateNotFoundException extends EntityNotFoundException {
    public PageTemplateNotFoundException(long id) {
        super(String.format("Page Template with stamp id %d is not found", id));
    }
}
