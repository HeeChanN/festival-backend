package com.halo.eventer.domain.splash.swagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.http.MediaType;

import com.halo.eventer.domain.splash.dto.SplashGetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "스플래시 레이어 전제 조회")
@ApiResponses(
        value = {
            @ApiResponse(
                    responseCode = "200",
                    content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SplashGetDto.class)))
        })
public @interface SplashGetApi {}
