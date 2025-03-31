package com.halo.eventer.domain.stamp;

import com.halo.eventer.domain.festival.Festival;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Stamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean stampOn = true;

    @Column(nullable = false)
    private Integer stampFinishCnt = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @OneToMany(mappedBy = "stamp", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StampUser> stampUsers = new ArrayList<>();

    @OneToMany(mappedBy = "stamp", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mission> missions = new ArrayList<>();

    private Stamp(Festival festival){
        this.festival = festival;
        festival.getStamps().add(this);
    }

    public void switchStampOn(){
        stampOn = !stampOn;
    }

    public void setStampFinishCnt(Integer cnt) { this.stampFinishCnt = cnt; }

    public static Stamp create(Festival festival){
        return new Stamp(festival);
    }
}
