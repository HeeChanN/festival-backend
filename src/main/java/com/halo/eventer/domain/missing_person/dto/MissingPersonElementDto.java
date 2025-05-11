package com.halo.eventer.domain.missing_person.dto;

import com.halo.eventer.domain.missing_person.MissingPerson;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissingPersonElementDto {
    private Long id;
    private String name;
    private Boolean popup;

    public MissingPersonElementDto(MissingPerson missingPerson) {
        this.id = missingPerson.getId();
        this.name = missingPerson.getName();
        this.popup = missingPerson.getPopup();
    }
}
