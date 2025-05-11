package com.halo.eventer.domain.map;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryImageDto;
import com.halo.eventer.domain.map.enumtype.MapCategoryType;
import com.halo.eventer.domain.widget.entity.DisplayOrderUpdatable;
import com.halo.eventer.global.constants.DisplayOrderConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "map_category")
public class MapCategory implements DisplayOrderUpdatable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;

    @Column(name = "icon", nullable = true)
    private String icon;

    @Column(name = "pin", nullable = true)
    private String pin;

    @Column(name = "display_order", nullable = false)
    private int displayOrder = DisplayOrderConstants.DISPLAY_ORDER_DEFAULT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @OneToMany(mappedBy = "mapCategory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Map> maps = new ArrayList<>();

    private MapCategory(Festival festival, String categoryName) {
        festival.applyMapCategory(this);
        this.festival = festival;
        this.categoryName = categoryName;
    }

    private MapCategory(MapCategoryType mapCategoryType) {
        this.categoryName = mapCategoryType.getDisplayName();
    }

    public static MapCategory of(Festival festival, String categoryName) {
        return new MapCategory(festival, categoryName);
    }

    public static MapCategory createFixedBooth() {
        return new MapCategory(MapCategoryType.FIXED_BOOTH);
    }

    public void updateIconAndPin(MapCategoryImageDto mapCategoryImageDto) {
        this.icon = mapCategoryImageDto.getIcon();
        this.pin = mapCategoryImageDto.getPin();
    }

    public void assignFestival(Festival festival) {
        this.festival = festival;
    }

    public void updateDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
