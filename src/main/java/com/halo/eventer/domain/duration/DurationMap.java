package com.halo.eventer.domain.duration;

import javax.persistence.*;

import com.halo.eventer.domain.map.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class DurationMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duration_id")
    private Duration duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id")
    private Map map;

    @Builder
    private DurationMap(Duration duration, Map map) {
        this.duration = duration;
        this.map = map;
    }

    public static DurationMap of(Duration duration, Map map) {
        return DurationMap.builder().duration(duration).map(map).build();
    }
}
