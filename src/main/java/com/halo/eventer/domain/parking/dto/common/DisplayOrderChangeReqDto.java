package com.halo.eventer.domain.parking.dto.common;

import java.util.List;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DisplayOrderChangeReqDto {
    private @NotNull List<@Min(1) Long> ids;
}
