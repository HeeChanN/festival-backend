package com.halo.eventer.domain.stamp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.*;

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

    private boolean finished = false;

    private int participantCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id")
    private Stamp stamp;

    @OneToMany(mappedBy = "stampUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserMission> userMissions = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
    private Custom custom;

    public StampUser(Stamp stamp, String encryptedPhone, String encryptedName, int participantCount) {
        this.stamp = stamp;
        this.uuid = UUID.randomUUID().toString();
        this.phone = encryptedPhone;
        this.name = encryptedName;
        this.finished = false;
        this.participantCount = participantCount;
    }

    public void finished() {
        this.finished = true;
    }

    public void setCustom(Custom custom) {
        this.custom = custom;
    }

    public void assignMissionsFrom(Stamp stamp) {
        List<UserMission> missions =
                stamp.getMissions().stream().map(m -> UserMission.from(m, this)).collect(Collectors.toList());
        this.userMissions = missions;
    }

    public boolean isAllMissionsCompleted() {
        return userMissions.stream().allMatch(UserMission::isComplete);
    }

    public void userMissionComplete(Long missionId) {
        this.userMissions.stream()
                .filter(m -> m.getId().equals(missionId))
                .findFirst()
                .orElseThrow(() -> new UserMissionNotFoundException(missionId))
                .updateComplete(true);
    }

    public boolean canFinishTour() {
        long completed = userMissions.stream().filter(UserMission::isComplete).count();
        return completed >= stamp.getStampFinishCnt();
    }
}
