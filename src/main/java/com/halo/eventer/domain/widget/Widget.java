package com.halo.eventer.domain.widget;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.widget.entity.DisplayOrderUpdatable;
import com.halo.eventer.domain.widget.properties.*;
import com.halo.eventer.domain.widget_item.WidgetItem;
import com.halo.eventer.global.common.BaseTime;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "widget")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Widget extends BaseTime implements DisplayOrderUpdatable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "widget_type", nullable = false)
    private WidgetType widgetType;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Type(JsonType.class)
    @Column(name = "properties", columnDefinition = "json")
    private WidgetProperties properties;

    @OneToMany(mappedBy = "widget", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<WidgetItem> widgetItems = new ArrayList<>();

    private Widget(Festival festival, String name, String url, WidgetType widgetType, WidgetProperties properties) {
        this.festival = festival;
        this.name = name;
        this.url = url;
        this.widgetType = widgetType;
        this.properties = properties;
    }

    private Widget(
            Festival festival,
            String name,
            String url,
            WidgetType widgetType,
            Integer displayOrder,
            WidgetProperties properties) {
        this.festival = festival;
        this.name = name;
        this.url = url;
        this.widgetType = widgetType;
        this.displayOrder = displayOrder;
        this.properties = properties;
    }

    public static Widget createMainWidget(
            Festival festival, String name, String url, String image, String description) {
        Widget widget = new Widget(festival, name, url, WidgetType.MAIN, new MainWidgetProperties(image, description));
        festival.applyWidget(widget);
        return widget;
    }

    public static Widget createUpWidget(
            Festival festival, String name, String url, LocalDateTime periodStart, LocalDateTime periodEnd) {
        Widget widget = new Widget(festival, name, url, WidgetType.UP, new UpWidgetProperties(periodStart, periodEnd));
        festival.applyWidget(widget);
        return widget;
    }

    public static Widget createMiddleWidget(
            Festival festival, String name, String url, String image, Integer displayOrder) {
        Widget widget =
                new Widget(festival, name, url, WidgetType.MIDDLE, displayOrder, new MiddleWidgetProperties(image));
        festival.applyWidget(widget);
        return widget;
    }

    public static Widget createSquareWidget(
            Festival festival, String name, String url, String image, String description, Integer displayOrder) {
        Widget widget = new Widget(
                festival, name, url, WidgetType.SQUARE, displayOrder, new SquareWidgetProperties(image, description));
        festival.applyWidget(widget);
        return widget;
    }

    public static Widget createDownWidget(Festival festival, String name, String url, Integer displayOrder) {
        Widget widget = new Widget(festival, name, url, WidgetType.DOWN, displayOrder, new DownWidgetProperties());
        festival.applyWidget(widget);
        return widget;
    }

    public <T extends WidgetProperties> T getTypedProperties(Class<T> type) {
        return type.cast(this.properties);
    }

    public void updateBaseField(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public void updateProperties(WidgetProperties properties) {
        this.properties = properties;
    }

    @Override
    public void updateDisplayOrder(Integer newOrder) {
        this.displayOrder = newOrder;
    }
}
