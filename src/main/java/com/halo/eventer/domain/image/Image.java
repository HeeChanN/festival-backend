package com.halo.eventer.domain.image;

import javax.persistence.*;

import com.halo.eventer.domain.notice.Notice;
import com.halo.eventer.domain.widget_item.WidgetItem;
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

    private Image(String imageUrl, Notice notice, WidgetItem widgetItem) {
        this.imageUrl = imageUrl;
        this.notice = notice;
        this.widgetItem = widgetItem;
    }

    public static Image ofNotice(String imageUrl, Notice notice) {
        return new Image(imageUrl, notice, null);
    }

    public static Image ofWidgetItem(String imageUrl, WidgetItem widgetItem) {
        return new Image(imageUrl, null, widgetItem);
    }
}
