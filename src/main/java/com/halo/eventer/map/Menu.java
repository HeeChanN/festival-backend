package com.halo.eventer.map;


import com.halo.eventer.map.dto.menu.MenuCreateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer price;

    private String summary;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id")
    private Map map;

    public Menu(MenuCreateDto menuCreateDto) {
        this.name = menuCreateDto.getName();
        this.price = menuCreateDto.getPrice();
        this.summary = menuCreateDto.getSummary();
        this.image = menuCreateDto.getImage();
    }

    public void setMenu(MenuCreateDto menuCreateDto) {
        this.name = menuCreateDto.getName();
        this.price = menuCreateDto.getPrice();
        this.summary = menuCreateDto.getSummary();
        this.image = menuCreateDto.getImage();
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
