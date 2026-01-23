package com.halo.eventer.domain.parking;

import java.util.Arrays;
import java.util.stream.Collectors;
import jakarta.persistence.*;

import com.halo.eventer.domain.parking.enums.CongestionLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "parking_lot")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    private String sido;
    private String sigungu;
    private String dongmyun;
    private String roadName;
    private String roadNumber;
    private String buildingName;

    private double latitude;
    private double longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "congestion_level", nullable = false)
    private CongestionLevel congestionLevel;

    @Column(name = "visible", nullable = false)
    private Boolean visible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_zone_id")
    private ParkingZone parkingZone;

    private ParkingLot(
            String name,
            String sido,
            String sigungu,
            String dongmyun,
            String roadName,
            String roadNumber,
            String buildingName,
            double latitude,
            double longitude,
            CongestionLevel congestionLevel,
            Boolean visible) {
        this.name = name;
        this.sido = sido;
        this.sigungu = sigungu;
        this.dongmyun = dongmyun;
        this.roadName = roadName;
        this.roadNumber = roadNumber;
        this.buildingName = buildingName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.congestionLevel = congestionLevel;
        this.visible = visible;
    }

    public static ParkingLot of(
            String name,
            String sido,
            String sigungu,
            String dongmyun,
            String roadName,
            String roadNumber,
            String buildingName,
            double latitude,
            double longitude,
            ParkingZone parkingZone) {
        ParkingLot parkingLot = new ParkingLot(
                name,
                sido,
                sigungu,
                dongmyun,
                roadName,
                roadNumber,
                buildingName,
                latitude,
                longitude,
                CongestionLevel.LOW,
                true);
        parkingLot.setParkingZone(parkingZone);
        return parkingLot;
    }

    public String getCopyAddress() {
        return joinWithSpace(this.sido, this.getSigungu(), this.getRoadName(), this.getRoadNumber());
    }

    public String getDisplayAddress() {
        String roadAddress = joinWithSpace(this.getRoadName(), this.getRoadNumber());
        return (buildingName == null || buildingName.isBlank()) ? roadAddress : roadAddress + " (" + buildingName + ")";
    }

    public void changeVisible(Boolean visible) {
        this.visible = visible;
    }

    public void changeCongestionLevel(String congestionLevel) {
        this.congestionLevel = CongestionLevel.valueOf(congestionLevel);
    }

    public void updateContent(
            String name,
            String sido,
            String sigungu,
            String dongmyun,
            String roadName,
            String roadNumber,
            String buildingName,
            double latitude,
            double longitude) {
        this.name = name;
        this.sido = sido;
        this.sigungu = sigungu;
        this.dongmyun = dongmyun;
        this.roadName = roadName;
        this.roadNumber = roadNumber;
        this.buildingName = buildingName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private void setParkingZone(ParkingZone parkingZone) {
        this.parkingZone = parkingZone;
        parkingZone.addParkingLot(this);
    }

    private static String joinWithSpace(String... parts) {
        return Arrays.stream(parts).filter(p -> p != null && !p.isBlank()).collect(Collectors.joining(" "));
    }
}
