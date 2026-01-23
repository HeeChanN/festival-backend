package com.halo.eventer.domain.parking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.parking.ParkingZone;

public interface ParkingZoneRepository extends JpaRepository<ParkingZone, Long> {

    @Query(
            "SELECT pz FROM ParkingZone pz JOIN FETCH pz.parkingManagement pm left join fetch pz.parkingLots pl WHERE pm.id = :parkingManagementId ")
    List<ParkingZone> findAllByParkingManagementId(@Param("parkingManagementId") Long parkingManagementId);

    @Query(
            "SELECT pz FROM ParkingZone pz JOIN FETCH pz.parkingManagement pm left join fetch pz.parkingLots pl WHERE pm.id = :parkingManagementId AND pz.visible = :visible ")
    List<ParkingZone> findAllByParkingManagementIdAndVisible(
            @Param("parkingManagementId") Long parkingManagementId, @Param("visible") Boolean visible);

    @Query("SELECT pz FROM ParkingZone pz left JOIN fetch pz.parkingLots pl where pz.id = :id ")
    Optional<ParkingZone> findByIdWithParkingLot(@Param("id") Long id);
}
