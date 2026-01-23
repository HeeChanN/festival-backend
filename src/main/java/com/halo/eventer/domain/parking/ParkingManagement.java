package com.halo.eventer.domain.parking;

import java.util.*;
import java.util.stream.Collectors;
import jakarta.persistence.*;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.image.Image;
import com.halo.eventer.domain.parking.enums.ParkingInfoType;
import com.halo.eventer.global.constants.DisplayOrderConstants;
import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.BaseException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "parking_management")
public class ParkingManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "header_name", nullable = false, length = 30)
    private String headerName;

    @Column(name = "parking_info_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ParkingInfoType parkingInfoType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "button_name", nullable = false)
    private String buttonName;

    @Column(name = "button_target_url", nullable = true)
    private String buttonTargetUrl;

    @Column(name = "background_image", nullable = true)
    private String backgroundImage;

    @Column(name = "sub_page_header_name", nullable = true)
    private String subPageHeaderName;

    @Column(name = "visible", nullable = false)
    private boolean visible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @OrderColumn(name = "display_order")
    @OneToMany(mappedBy = "parkingManagement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OrderColumn(name = "display_order")
    @OneToMany(mappedBy = "parkingManagement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkingZone> parkingZones = new ArrayList<>();

    @OneToMany(mappedBy = "parkingManagement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ParkingNotice> parkingNotices = new ArrayList<>();

    private ParkingManagement(
            Festival festival,
            String headerName,
            ParkingInfoType parkingInfoType,
            String title,
            String description,
            String buttonName,
            String buttonTargetUrl,
            String backgroundImage,
            boolean visible) {
        this.festival = festival;
        this.headerName = headerName;
        this.parkingInfoType = parkingInfoType;
        this.title = title;
        this.description = description;
        this.buttonName = buttonName;
        this.buttonTargetUrl = buttonTargetUrl;
        this.backgroundImage = backgroundImage;
        this.visible = visible;
    }

    public static ParkingManagement of(
            Festival festival,
            String headerName,
            ParkingInfoType parkingInfoType,
            String title,
            String description,
            String buttonName,
            String buttonTargetUrl,
            String backgroundImage,
            boolean visible) {
        ParkingManagement parkingManagement = new ParkingManagement(
                festival,
                headerName,
                parkingInfoType,
                title,
                description,
                buttonName,
                buttonTargetUrl,
                backgroundImage,
                visible);
        festival.getParkingManagements().add(parkingManagement);
        return parkingManagement;
    }

    public void update(
            String headerName,
            ParkingInfoType parkingInfoType,
            String title,
            String description,
            String buttonName,
            String buttonTargetUrl,
            String backgroundImage,
            boolean visible) {
        this.headerName = headerName;
        this.parkingInfoType = parkingInfoType;
        this.title = title;
        this.description = description;
        this.buttonName = buttonName;
        this.buttonTargetUrl = buttonTargetUrl;
        this.backgroundImage = backgroundImage;
        this.visible = visible;
    }

    public void updateSubPageHeaderName(String subPageHeaderName) {
        this.subPageHeaderName = subPageHeaderName;
    }

    public void addImage(String imageUrl) {
        validateImageCount();
        Image image = Image.ofParkingManagement(imageUrl, this);
        images.add(image);
    }

    public void addParkingNotice(ParkingNotice parkingNotice) {
        parkingNotices.add(parkingNotice);
    }

    public void addParkingZone(ParkingZone parkingZone) {
        validateZoneCount();
        parkingZones.add(parkingZone);
    }

    public void reorderParkingZones(List<Long> orderedIds) {
        validateReorderIdsDuplicationAndSizeForZone(orderedIds);
        validateNoMissingIdsForZone(orderedIds);

        Map<Long, Integer> rank = new HashMap<>();
        for (int i = 0; i < orderedIds.size(); i++) {
            rank.put(orderedIds.get(i), i);
        }
        parkingZones.sort(Comparator.comparingInt((ParkingZone zone) -> rank.get(zone.getId())));
    }

    public void removeParkingZone(Long parkingZoneId) {
        parkingZones.removeIf(parkingZone -> parkingZoneId == parkingZone.getId());
    }

    public void reorderImages(List<Long> orderedIds) {
        validateReorderIdsDuplicationAndSize(orderedIds);
        validateNoMissingIds(orderedIds);

        Map<Long, Integer> rank = new HashMap<>();
        for (int i = 0; i < orderedIds.size(); i++) {
            rank.put(orderedIds.get(i), i);
        }
        images.sort(Comparator.comparingInt((Image img) -> rank.get(img.getId())));
    }

    public void removeImages(List<Long> imageIds) {
        Set<Long> removedIds = new HashSet<>(imageIds);
        images.removeIf(image -> removedIds.contains(image.getId()));
    }

    private void validateReorderIdsDuplicationAndSize(List<Long> orderedIds) {
        Set<Long> targets = new HashSet<>(orderedIds);
        if (targets.size() != orderedIds.size() || targets.size() != images.size()) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private void validateNoMissingIds(List<Long> orderedIds) {
        Set<Long> existingIds = images.stream().map(Image::getId).collect(Collectors.toSet());
        List<Long> unknown =
                orderedIds.stream().filter(id -> !existingIds.contains(id)).toList();
        if (!unknown.isEmpty()) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private void validateImageCount() {
        if (images.size() >= DisplayOrderConstants.DISPLAY_ORDER_LIMIT_VALUE) {
            throw new BaseException(ErrorCode.MAX_COUNT_EXCEEDED);
        }
    }

    private void validateReorderIdsDuplicationAndSizeForZone(List<Long> orderedIds) {
        Set<Long> targets = new HashSet<>(orderedIds);
        if (targets.size() != orderedIds.size() || targets.size() != parkingZones.size()) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private void validateNoMissingIdsForZone(List<Long> orderedIds) {
        Set<Long> existingIds = parkingZones.stream().map(ParkingZone::getId).collect(Collectors.toSet());
        List<Long> unknown =
                orderedIds.stream().filter(id -> !existingIds.contains(id)).toList();
        if (!unknown.isEmpty()) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private void validateZoneCount() {
        if (parkingZones.size() >= DisplayOrderConstants.DISPLAY_ORDER_LIMIT_VALUE) {
            throw new BaseException(ErrorCode.MAX_COUNT_EXCEEDED);
        }
    }
}
