package com.halo.eventer.domain.map;

import javax.persistence.*;

import com.halo.eventer.domain.map.dto.menu.MenuCreateDto;
import com.halo.eventer.domain.map.dto.menu.MenuUpdateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void updateMenu(MenuUpdateDto menuUpdateDto) {
        this.name = menuUpdateDto.getName();
        this.price = menuUpdateDto.getPrice();
        this.summary = menuUpdateDto.getSummary();
        this.image = menuUpdateDto.getImage();
    }
}
