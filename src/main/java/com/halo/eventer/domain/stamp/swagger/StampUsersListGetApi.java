package com.halo.eventer.domain.stamp.swagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.http.MediaType;

import com.halo.eventer.domain.stamp.dto.stamp.StampUsersGetListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "스탬프별 유저 전체 조회", description = "스탬프 id로 유저 리스트 조회")
@ApiResponses(
        value = {
            @ApiResponse(
                    responseCode = "200",
                    content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StampUsersGetListDto.class)))
        })
public @interface StampUsersListGetApi {}
