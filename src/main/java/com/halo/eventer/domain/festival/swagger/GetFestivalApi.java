package com.halo.eventer.domain.festival.swagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.http.MediaType;

import com.halo.eventer.domain.festival.dto.FestivalResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "축제 조회", description = "(임시) 축제 id로 조회")
@ApiResponses(
        value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "축제 정보 조회 성공",
                    content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = FestivalResDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "데이터가 존재하지 않을 경우 예외",
                    content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {@ExampleObject(value = "존재하지 않습니다")}))
        })
public @interface GetFestivalApi {}
