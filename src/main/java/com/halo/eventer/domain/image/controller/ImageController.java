package com.halo.eventer.domain.image.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.halo.eventer.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/upload/preSigned")
    public String getUploadUrl(@RequestParam String fileExtension) {
        return imageService.generatePreSignedUploadUrl(fileExtension);
    }
}
