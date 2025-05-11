package com.halo.eventer.domain.festival.swagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.http.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "축제 색 등록", description = "축제 id로 축제 색 등록")
@ApiResponses(
        value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "축제 색 등록 완료",
                    content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {@ExampleObject(name = "축제 색 등록 완료", value = "색 등록 완료")})),
            @ApiResponse(
                    responseCode = "400",
                    description = "축제 정보 존재 X",
                    content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {@ExampleObject(name = "축제 정보 없음", value = "축제 정보가 존재하지 않습니다.")}))
        })
public @interface FestivalColorAddApi {}
