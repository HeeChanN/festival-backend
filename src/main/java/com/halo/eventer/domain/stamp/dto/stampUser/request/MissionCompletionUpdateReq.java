package com.halo.eventer.domain.stamp.dto.stampUser.request;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionCompletionUpdateReq {
    @NotNull
    private boolean finished;

    private String receivedPrizeName;
}
