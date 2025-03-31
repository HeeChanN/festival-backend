package com.halo.eventer.domain.map.exception;

import com.halo.eventer.global.error.exception.InvalidInputException;

public class DurationIdsInvalidInputException extends InvalidInputException {
    public DurationIdsInvalidInputException() {
        super("Some of the requested Duration IDs do not exist.");
    }
}
