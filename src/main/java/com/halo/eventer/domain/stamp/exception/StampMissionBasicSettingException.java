package com.halo.eventer.domain.stamp.exception;

import com.halo.eventer.global.error.exception.EntityNotFoundException;

public class StampMissionBasicSettingException extends EntityNotFoundException {
    public StampMissionBasicSettingException(long id) {
        super(String.format("Stamp Mission Settings with stamp id %d is not found", id));
    }
}
