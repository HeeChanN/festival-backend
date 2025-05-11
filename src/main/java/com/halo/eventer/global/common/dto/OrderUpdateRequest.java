package com.halo.eventer.global.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderUpdateRequest {
    private Long id;
    private int displayOrder;

    private OrderUpdateRequest(Long id, int displayOrder) {
        this.id = id;
        this.displayOrder = displayOrder;
    }

    public static OrderUpdateRequest of(Long id, int displayOrder) {
        return new OrderUpdateRequest(id, displayOrder);
    }
}
