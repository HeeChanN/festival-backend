package com.halo.eventer.global.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderUpdateRequest {

    @Min(1)
    private Long id;

    @Max(11)
    private int displayOrder;

    private OrderUpdateRequest(Long id, int displayOrder) {
        this.id = id;
        this.displayOrder = displayOrder;
    }

    public static OrderUpdateRequest of(Long id, int displayOrder) {
        return new OrderUpdateRequest(id, displayOrder);
    }
}
