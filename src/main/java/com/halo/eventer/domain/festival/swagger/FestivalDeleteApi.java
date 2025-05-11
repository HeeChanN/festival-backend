package com.halo.eventer.domain.festival.swagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.http.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "축제 삭제", description = "축제 id로 삭제")
@ApiResponse(
        responseCode = "200",
        description = "삭제 완료되었습니다.",
        content =
                @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        examples = {@ExampleObject(name = "삭제 완료 예제", value = "삭제완료")}))
public @interface FestivalDeleteApi {}
