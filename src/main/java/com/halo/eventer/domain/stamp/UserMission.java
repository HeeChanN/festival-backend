package com.halo.eventer.domain.stamp;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "user_mission",
        indexes = {@Index(name = "idx_stamp_user_id", columnList = "stamp_user_id")})
public class UserMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isComplete = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_user_id")
    private StampUser stampUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    public void markAsComplete() {
        this.isComplete = true;
    }

    private void setStampUser(StampUser stampUser) {
        this.stampUser = stampUser;
        stampUser.getUserMissions().add(this);
    }

    private void setMission(Mission mission) {
        this.mission = mission;
    }

    public static UserMission create(Mission mission, StampUser stampUser) {
        UserMission userMission = new UserMission();
        userMission.setStampUser(stampUser);
        userMission.setMission(mission);
        return userMission;
    }
}
