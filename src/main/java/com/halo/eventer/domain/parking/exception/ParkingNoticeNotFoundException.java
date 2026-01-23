package com.halo.eventer.domain.parking.exception;

import com.halo.eventer.global.error.exception.EntityNotFoundException;

public class ParkingNoticeNotFoundException extends EntityNotFoundException {
    public ParkingNoticeNotFoundException(Long id) {
        super(String.format("ParkingNotice with %d is not found", id));
    }
}
