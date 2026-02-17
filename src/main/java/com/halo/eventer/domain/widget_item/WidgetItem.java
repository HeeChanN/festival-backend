package com.halo.eventer.domain.widget_item;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

import com.halo.eventer.domain.image.Image;
import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget_item.dto.WidgetItemCreateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class WidgetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String icon;

    @OneToMany(
            mappedBy = "widgetItem",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Image> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "widget_id")
    private Widget widget;

    @Builder
    public WidgetItem(String name, String description, String icon, Widget widget) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.widget = widget;
    }

    public static WidgetItem from(Widget widget, WidgetItemCreateDto widgetItemCreateDto) {
        return WidgetItem.builder()
                .widget(widget)
                .name(widgetItemCreateDto.getName())
                .description(widgetItemCreateDto.getDescription())
                .icon(widgetItemCreateDto.getIcon())
                .build();
    }

    public void updateWidgetItem(WidgetItemCreateDto widgetItemCreateDto) {
        this.name = widgetItemCreateDto.getName();
        this.description = widgetItemCreateDto.getDescription();
        this.icon = widgetItemCreateDto.getIcon();
    }

    public void addImages(String url) {
        Image image = Image.ofWidgetItem(url, this);
        this.images.add(image);
    }
}
