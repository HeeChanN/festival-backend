package com.halo.eventer.domain.stamp.exception;

import com.halo.eventer.global.error.exception.InvalidInputException;

public class StampNotInFestivalException extends InvalidInputException {
    public StampNotInFestivalException(Long stampId, Long festivalId) {
        super(String.format("Stamp %s is not in Festival %s", stampId, festivalId));
    }
}
