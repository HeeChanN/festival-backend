package com.halo.eventer.domain.home.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.halo.eventer.domain.festival.dto.FestivalSummaryDto;
import com.halo.eventer.domain.festival.service.FestivalService;
import com.halo.eventer.domain.home.dto.HomeDto;
import com.halo.eventer.domain.home.service.HomeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final FestivalService festivalService;

    @GetMapping("/home/{festivalId}")
    public HomeDto getHomeInfo(@Min(1) @PathVariable Long festivalId) {
        return homeService.getMainPage(festivalId);
    }

    @GetMapping("/home")
    public FestivalSummaryDto getFestivalBySubAddress(@NotNull @RequestParam("subAddress") String name) {
        return festivalService.findBySubAddress(name);
    }

    @GetMapping("/")
    public void getOk() {}
}
