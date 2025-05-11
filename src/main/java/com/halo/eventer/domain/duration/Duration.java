package com.halo.eventer.domain.duration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.halo.eventer.domain.duration.dto.DurationCreateDto;
import com.halo.eventer.domain.festival.Festival;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(
        name = "duration",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"festival_id", "date"})})
public class Duration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "day_number")
    private Integer dayNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @OneToMany(mappedBy = "duration", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<DurationMap> durationMaps = new ArrayList<>();

    @Builder
    public Duration(LocalDate date, Integer dayNumber, Festival festival) {
        this.date = date;
        this.dayNumber = dayNumber;
        this.festival = festival;
    }

    public static Duration of(Festival festival, DurationCreateDto durationCreateDto) {
        return Duration.builder()
                .date(durationCreateDto.getDate())
                .dayNumber(durationCreateDto.getDayNumber())
                .festival(festival)
                .build();
    }
}
