package com.halo.eventer.global.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.halo.eventer.domain.widget.entity.DisplayOrderUpdatable;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DisplayOrderUtils {

    public static <T extends DisplayOrderUpdatable, R extends OrderUpdateRequest> void updateDisplayOrder(
            List<T> widgets, List<R> orderRequests) {
        Map<Long, Integer> orderMap = orderRequests.stream().collect(Collectors.toMap(R::getId, R::getDisplayOrder));

        widgets.stream()
                .filter(w -> orderMap.containsKey(w.getId()))
                .forEach(w -> w.updateDisplayOrder(orderMap.get(w.getId())));
    }
}
