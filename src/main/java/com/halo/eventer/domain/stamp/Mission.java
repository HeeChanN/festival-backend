package com.halo.eventer.domain.stamp;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.dto.mission.request.MissionSetReqDto;
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
    private Boolean showMission = true;
    private Boolean showTitle = true;
    private Integer requiredSuccessCount = 1;
    private Boolean showRequiredSuccessCount = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id", nullable = false)
    private Stamp stamp;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserMission> userMissions = new ArrayList<>();

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MissionDetailsTemplate> missionDetailsTemplates = new ArrayList<>();

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

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateClearedImage(String clearedThumbnail, String notClearedThumbnail) {
        this.clearedThumbnail = clearedThumbnail;
        this.notClearedThumbnail = notClearedThumbnail;
    }

    public void updateTitleVisible(boolean showTitle) {
        this.showTitle = showTitle;
    }

    public void updateRequiredSuccessCount(int successCount) {
        this.requiredSuccessCount = successCount;
    }

    public void updateRequiredSuccessCountVisible(boolean showSuccessCount) {
        this.showRequiredSuccessCount = showSuccessCount;
    }

    public void updateMissionShow(boolean show) {
        this.showMission = show;
    }

    public static Mission from(MissionSetReqDto request) {
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

    public static Mission from(Stamp stamp, String missionName, Boolean showMission) {
        Mission mission = Mission.builder().title(missionName).build();
        mission.addStamp(stamp);
        mission.updateMissionShow(showMission);
        return mission;
    }
}
