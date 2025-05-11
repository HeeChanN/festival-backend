package com.halo.eventer.domain.missing_person.dto;

import com.halo.eventer.domain.missing_person.MissingPerson;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MissingPersonPopupDto {
    private String name;
    private String age;
    private String gender;
    private String thumbnail;
    private String content;
    private String missingLocation;

    public MissingPersonPopupDto(MissingPerson m) {
        this.name = m.getName();
        this.age = m.getAge();
        this.gender = m.getGender();
        this.thumbnail = m.getThumbnail();
        this.content = m.getContent();
        this.missingLocation = m.getMissingLocation();
    }
}
