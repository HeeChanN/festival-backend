package com.halo.eventer.domain.image;

import jakarta.persistence.*;

import com.halo.eventer.domain.notice.Notice;
import com.halo.eventer.domain.parking.ParkingManagement;
import com.halo.eventer.domain.widget_item.WidgetItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "widget_item_id")
    private WidgetItem widgetItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_management_id")
    private ParkingManagement parkingManagement;

    @Builder
    private Image(String imageUrl, Notice notice, WidgetItem widgetItem, ParkingManagement parkingManagement) {
        this.imageUrl = imageUrl;
        this.notice = notice;
        this.widgetItem = widgetItem;
        this.parkingManagement = parkingManagement;
    }

    public static Image ofNotice(String imageUrl, Notice notice) {
        return Image.builder().imageUrl(imageUrl).notice(notice).build();
    }

    public static Image ofWidgetItem(String imageUrl, WidgetItem widgetItem) {
        return Image.builder().imageUrl(imageUrl).widgetItem(widgetItem).build();
    }

    public static Image ofParkingManagement(String imageUrl, ParkingManagement parkingManagement) {
        return Image.builder()
                .imageUrl(imageUrl)
                .parkingManagement(parkingManagement)
                .build();
    }
}
