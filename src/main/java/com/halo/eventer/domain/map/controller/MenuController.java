package com.halo.eventer.domain.map.controller;

import com.halo.eventer.domain.map.dto.menu.MenuCreateDto;
import com.halo.eventer.domain.map.dto.menu.MenuResDto;
import com.halo.eventer.domain.map.service.MenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "메뉴")
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;

    @PostMapping()
    public ResponseEntity<?> createMenu(@RequestBody List<MenuCreateDto> menuCreateDtos,
                                        @RequestParam("storeId")Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(menuService.createMenu(menuCreateDtos, id));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> getMenus(@RequestParam("storeId")Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(menuService.getMenus(id));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }


    @PatchMapping()
    public List<MenuResDto> updateMenu(@RequestParam("mapId")Long id,
                                       @RequestBody List<MenuResDto> menuCreateDtos){
        return menuService.updateMenu(id,menuCreateDtos);
    }


    @DeleteMapping("/{menuId}")
    public ResponseEntity<?> deleteMenu(@PathVariable("menuId") Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(menuService.deleteMenu(id));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
