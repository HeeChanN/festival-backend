package com.halo.eventer.domain.parking.exception;

import com.halo.eventer.global.error.exception.EntityNotFoundException;

public class ParkingManagementNotFoundException extends EntityNotFoundException {
    public ParkingManagementNotFoundException(Long id) {
        super(String.format("ParkingManagement with %d is not found", id));
    }
}
