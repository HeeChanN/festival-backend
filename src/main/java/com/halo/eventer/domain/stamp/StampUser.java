package com.halo.eventer.domain.stamp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;

import com.halo.eventer.domain.stamp.exception.UserMissionNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(
        name = "stamp_user",
        indexes = {
            @Index(name = "idx_uuid", columnList = "uuid"),
            @Index(name = "idx_phone_name_stamp", columnList = "phone, name, stamp_id")
        })
public class StampUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uuid = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String name;

    private boolean isFinished = false;

    private int participantCount;

    @Column(nullable = true)
    private String schoolNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id")
    private Stamp stamp;

    @OneToMany(mappedBy = "stampUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserMission> userMissions = new ArrayList<>();

    // TODO : Custom 엔티티를 없애고 schoolNo라는 필드를 StampUser에 정의하고 null을 하용하는 방식의 설계 고려 가능
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
    private Custom custom;

    public StampUser(String encryptedPhone, String encryptedName, int participantCount) {
        this.phone = encryptedPhone;
        this.name = encryptedName;
        this.isFinished = false;
        this.participantCount = participantCount;
    }

    public StampUser(String encryptedPhone, String encryptedName, int participantCount, String schoolNo) {
        this.phone = encryptedPhone;
        this.name = encryptedName;
        this.isFinished = false;
        this.participantCount = participantCount;
        this.schoolNo = schoolNo;
    }

    public void addStamp(Stamp stamp) {
        this.stamp = stamp;
        stamp.getStampUsers().add(this);
    }

    public void markAsFinished() {
        this.isFinished = true;
    }

    public void setCustom(Custom custom) {
        this.custom = custom;
    }

    public void assignUserMissions(List<UserMission> userMissions) {
        this.userMissions = userMissions;
    }

    public boolean isMissionsAllCompleted() {
        return userMissions.stream().allMatch(UserMission::isComplete);
    }

    public void completeUserMission(Long userMissionId) {
        this.userMissions.stream()
                .filter(userMission -> userMission.getId().equals(userMissionId))
                .findFirst()
                .orElseThrow(() -> new UserMissionNotFoundException(userMissionId))
                .markAsComplete();
    }

    public boolean canFinishTour() {
        long completed = userMissions.stream().filter(UserMission::isComplete).count();
        return completed >= stamp.getFinishCount();
    }
}
