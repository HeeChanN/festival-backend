package com.halo.eventer.domain.stamp;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class StampMissionPrize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "required_count", nullable = false)
    private Integer requiredCount;

    @Column(nullable = false, length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id")
    private Stamp stamp;

    @Builder
    private StampMissionPrize(int requiredCount, String description, Stamp stamp) {
        this.requiredCount = requiredCount;
        this.description = description;
        this.stamp = stamp;
        registerSettings(stamp);
    }

    private void registerSettings(Stamp stamp) {
        this.stamp = stamp;
        stamp.getPrizes().add(this);
    }

    public void update(int requiredCount, String description) {
        this.requiredCount = requiredCount;
        this.description = description;
    }

    public static StampMissionPrize from(int requiredCount, String description, Stamp stamp) {
        return StampMissionPrize.builder()
                .description(description)
                .requiredCount(requiredCount)
                .stamp(stamp)
                .build();
    }
}
