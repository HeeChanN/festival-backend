package com.halo.eventer.domain.parking;

import java.util.*;
import java.util.stream.Collectors;
import jakarta.persistence.*;

import com.halo.eventer.global.constants.DisplayOrderConstants;
import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.BaseException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "parking_zone")
public class ParkingZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean visible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_management_id")
    private ParkingManagement parkingManagement;

    @OrderColumn(name = "display_order")
    @OneToMany(mappedBy = "parkingZone", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkingLot> parkingLots = new ArrayList<>();

    private ParkingZone(String name, Boolean visible) {
        this.name = name;
        this.visible = visible;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void changeVisible(Boolean visible) {
        this.visible = visible;
    }

    public void changeAllVisible(Boolean visible) {
        this.parkingLots.forEach(parkingLot -> parkingLot.changeVisible(visible));
    }

    public static ParkingZone of(String name, Boolean visible, ParkingManagement parkingManagement) {
        ParkingZone parkingZone = new ParkingZone(name, visible);
        parkingZone.setParkingManagement(parkingManagement);
        return parkingZone;
    }

    public void addParkingLot(ParkingLot parkingLot) {
        validateParkingLotCount();
        this.parkingLots.add(parkingLot);
    }

    private void setParkingManagement(ParkingManagement parkingManagement) {
        this.parkingManagement = parkingManagement;
        parkingManagement.addParkingZone(this);
    }

    public void reorderParkingLot(List<Long> orderedIds) {
        validateReorderIdsDuplicationAndSize(orderedIds);
        validateNoMissingIds(orderedIds);

        Map<Long, Integer> rank = new HashMap<>();
        for (int i = 0; i < orderedIds.size(); i++) {
            rank.put(orderedIds.get(i), i);
        }
        parkingLots.sort(Comparator.comparingInt((ParkingLot parkingLot) -> rank.get(parkingLot.getId())));
    }

    public void removeParkingLot(Long parkingLotId) {
        parkingLots.removeIf(parkingLot -> parkingLotId == parkingLot.getId());
    }

    private void validateReorderIdsDuplicationAndSize(List<Long> orderedIds) {
        Set<Long> targets = new HashSet<>(orderedIds);
        if (targets.size() != orderedIds.size() || targets.size() != parkingLots.size()) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private void validateNoMissingIds(List<Long> orderedIds) {
        Set<Long> existingIds = parkingLots.stream().map(ParkingLot::getId).collect(Collectors.toSet());
        List<Long> unknown =
                orderedIds.stream().filter(id -> !existingIds.contains(id)).toList();
        if (!unknown.isEmpty()) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private void validateParkingLotCount() {
        if (parkingLots.size() >= DisplayOrderConstants.DISPLAY_ORDER_LIMIT_VALUE) {
            throw new BaseException(ErrorCode.MAX_COUNT_EXCEEDED);
        }
    }
}
