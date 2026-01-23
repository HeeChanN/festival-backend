package com.halo.eventer.domain.parking.exception;

import com.halo.eventer.global.error.exception.EntityNotFoundException;

public class ParkingZoneNotFoundException extends EntityNotFoundException {
    public ParkingZoneNotFoundException(Long id) {
        super(String.format("ParkingZone with %d is not found", id));
    }
}
