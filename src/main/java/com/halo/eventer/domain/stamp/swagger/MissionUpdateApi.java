package com.halo.eventer.domain.stamp.swagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "단일 미션 수정", description = "missionId로 조회")
@ApiResponses(
        value = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = String.class)))
        })
public @interface MissionUpdateApi {}
