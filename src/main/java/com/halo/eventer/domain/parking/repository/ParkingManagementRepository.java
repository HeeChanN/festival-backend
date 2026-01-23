package com.halo.eventer.domain.parking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.parking.ParkingManagement;

public interface ParkingManagementRepository extends JpaRepository<ParkingManagement, Long> {

    @Query("SELECT pm FROM ParkingManagement pm LEFT JOIN FETCH pm.images i WHERE pm.id = :id ")
    Optional<ParkingManagement> findByIdWithImages(@Param("id") Long id);

    Optional<ParkingManagement> findByIdAndVisible(Long id, Boolean visible);

    @Query("SELECT pm FROM ParkingManagement pm LEFT JOIN FETCH pm.parkingZones WHERE pm.id = :id ")
    Optional<ParkingManagement> findByIdWithParkingZone(@Param("id") Long id);

    @Query("SELECT pm FROM ParkingManagement pm WHERE pm.festival.id = :festivalId ")
    List<ParkingManagement> findByFestivalId(@Param("festivalId") Long festivalId);
}
