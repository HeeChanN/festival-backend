package com.halo.eventer.domain.parking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.halo.eventer.domain.parking.ParkingNotice;

public interface ParkingNoticeRepository extends JpaRepository<ParkingNotice, Long> {

    @Query("SELECT pn FROM ParkingNotice pn WHERE pn.parkingManagement.id = :parkingManagementId ")
    List<ParkingNotice> findByIdParkingManagementId(@Param("parkingManagementId") Long parkingManagementId);

    @Query(
            "SELECT pn FROM ParkingNotice pn WHERE pn.parkingManagement.id = :parkingManagementId AND pn.visible = :visible ")
    List<ParkingNotice> findByIdParkingManagementIdAndVisible(
            @Param("parkingManagementId") Long parkingManagementId, @Param("visible") Boolean visible);
}
