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
@Operation(summary = "축제 등록", description = "축제 정보 등록")
@ApiResponses(
        value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "저장이 완료되었습니다.",
                    content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {@ExampleObject(value = "저장완료")})),
            @ApiResponse(
                    responseCode = "400",
                    description = "데이터가 이미 존재할 경우",
                    content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {@ExampleObject(value = "중복생성")}))
        })
public @interface FestivalCreateApi {}
