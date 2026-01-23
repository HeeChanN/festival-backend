package com.halo.eventer.domain.parking;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parking_notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ParkingNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "visible", nullable = false)
    private Boolean visible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_management_id")
    private ParkingManagement parkingManagement;

    private ParkingNotice(String title, String content) {
        this.title = title;
        this.content = content;
        this.visible = true;
    }

    public void updateNotice(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void changeVisible(Boolean visible) {
        this.visible = visible;
    }

    public static ParkingNotice of(String title, String content, ParkingManagement parkingManagement) {
        ParkingNotice parkingNotice = new ParkingNotice(title, content);
        parkingNotice.setParkingManagement(parkingManagement);
        return parkingNotice;
    }

    private void setParkingManagement(ParkingManagement parkingManagement) {
        this.parkingManagement = parkingManagement;
        parkingManagement.addParkingNotice(this);
    }
}
