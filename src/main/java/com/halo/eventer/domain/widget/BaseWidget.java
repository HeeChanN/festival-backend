package com.halo.eventer.domain.widget;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.widget_item.WidgetItem;
import com.halo.eventer.global.common.BaseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "widget_type")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseWidget extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "widget_type", insertable = false, updatable = false)
    private String widgetType;

    @OneToMany(mappedBy = "baseWidget", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<WidgetItem> widgetItems = new ArrayList<>();

    protected BaseWidget(Festival festival, String name, String url) {
        this.festival = festival;
        this.name = name;
        this.url = url;
    }

    public void updateBaseField(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
