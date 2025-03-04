package com.halo.eventer.domain.stamp.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "담당자 확인 버튼", description = "finished 업데이트 api")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                content = @Content(schema = @Schema(implementation = String.class)))
})
public @interface StampCheckApi {
}
