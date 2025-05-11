package com.halo.eventer.domain.widget.feature;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class PeriodFeature {

    @Column(name = "period_start")
    private LocalDateTime periodStart;

    @Column(name = "period_end")
    private LocalDateTime periodEnd;

    private PeriodFeature(LocalDateTime periodStart, LocalDateTime periodEnd) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    public static PeriodFeature of(LocalDateTime periodStart, LocalDateTime periodEnd) {
        return new PeriodFeature(periodStart, periodEnd);
    }
}
