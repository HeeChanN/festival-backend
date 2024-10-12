package com.halo.eventer.domain.stamp;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "stamp_user", indexes = {
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

    private boolean finished;

    private int participantCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_id")
    private Stamp stamp;

    @OneToMany(mappedBy = "stampUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserMission> userMissions;

    public StampUser(Stamp stamp, String encryptedPhone, String encryptedName, int participantCount) {
        this.stamp = stamp;
        this.uuid = UUID.randomUUID().toString();
        this.phone = encryptedPhone;
        this.name = encryptedName;
        this.finished = false;
        this.participantCount = participantCount;
    }

    public void setFinished() { this.finished = true; }

    public void setUserMission(List<UserMission> userMissions) { this.userMissions = userMissions; }
}
