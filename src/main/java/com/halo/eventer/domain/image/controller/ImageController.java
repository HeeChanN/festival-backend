package com.halo.eventer.domain.image.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.halo.eventer.domain.image.service.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "이미지 업로드", description = "각 부분에 대한 이미지 업로드")
public class ImageController {

    private final ImageService imageService;

    // Pre Signed URL 방식
    @GetMapping("/upload/preSigned")
    public String getUploadUrl(@RequestParam String fileExtension) {
        return imageService.generatePreSignedUploadUrl(fileExtension);
    }
}
