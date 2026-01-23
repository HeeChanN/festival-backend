package com.halo.eventer.domain.parking.exception;

import com.halo.eventer.global.error.exception.EntityNotFoundException;

public class ParkingLotNotFoundException extends EntityNotFoundException {
    public ParkingLotNotFoundException(Long id) {
        super(String.format("ParkingLot with %d is not found", id));
    }
}
