package com.halo.eventer.domain.widget.properties;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpWidgetProperties implements WidgetProperties {
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
}
