package com.halo.eventer.domain.missing_person;

import javax.persistence.*;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.missing_person.dto.MissingPersonReqDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class MissingPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String age;
    private String gender;
    private String thumbnail;
    private String missingLocation;
    private String missingTime;
    private String content;

    private String parentName;
    private String parentNo;
    private Boolean popup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    public MissingPerson(MissingPersonReqDto m, Festival festival) {
        this.name = m.getName();
        this.age = m.getAge();
        this.gender = m.getGender();
        this.thumbnail = m.getThumbnail();
        this.missingLocation = m.getMissingLocation();
        this.missingTime = m.getMissingTime();
        this.content = m.getContent();
        this.parentName = m.getParentName();
        this.parentNo = m.getParentNo();
        this.popup = false;
        this.festival = festival;
    }

    public void update(MissingPersonReqDto m) {
        this.name = m.getName();
        this.age = m.getAge();
        this.gender = m.getGender();
        this.thumbnail = m.getThumbnail();
        this.missingLocation = m.getMissingLocation();
        this.missingTime = m.getMissingTime();
        this.content = m.getContent();
        this.parentName = m.getParentName();
        this.parentNo = m.getParentNo();
    }

    public void setPopup(Boolean popup) {
        this.popup = popup;
    }
}
