package com.halo.eventer.domain.stamp;

import jakarta.persistence.*;

import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.dto.stamp.MissionSetDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(indexes = {@Index(name = "idx_stamp_id", columnList = "stamp_id")})
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long boothId;

    private String title;

    private String content;

    private String place;

    private String time;

    private String clearedThumbnail;

    private String notClearedThumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id")
    private Stamp stamp;

    @Builder
    private Mission(
            Long boothId,
            String title,
            String content,
            String place,
            String time,
            String clearedThumbnail,
            String notClearedThumbnail) {
        this.boothId = boothId;
        this.title = title;
        this.content = content;
        this.place = place;
        this.time = time;
        this.clearedThumbnail = clearedThumbnail;
        this.notClearedThumbnail = notClearedThumbnail;
    }

    public void updateMission(MissionUpdateDto request) {
        if (request.getBoothId() != null) this.boothId = request.getBoothId();
        if (request.getTitle() != null) this.title = request.getTitle();
        if (request.getContent() != null) this.content = request.getContent();
        if (request.getPlace() != null) this.place = request.getPlace();
        if (request.getTime() != null) this.time = request.getTime();
        if (request.getClearedThumbnail() != null) this.clearedThumbnail = request.getClearedThumbnail();
        if (request.getNotClearedThumbnail() != null) this.notClearedThumbnail = request.getNotClearedThumbnail();
    }

    public void addStamp(Stamp stamp) {
        this.stamp = stamp;
        stamp.getMissions().add(this);
    }

    public static Mission from(MissionSetDto request) {
        return Mission.builder()
                .boothId(request.getBoothId())
                .title(request.getTitle())
                .content(request.getContent())
                .place(request.getPlace())
                .time(request.getTime())
                .clearedThumbnail(request.getClearedThumbnail())
                .notClearedThumbnail(request.getNotClearedThumbnail())
                .build();
    }
}
