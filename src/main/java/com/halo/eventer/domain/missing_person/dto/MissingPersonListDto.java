package com.halo.eventer.domain.missing_person.dto;


import com.halo.eventer.domain.missing_person.MissingPerson;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MissingPersonListDto {
    private List<MissingPersonElementDto> missingPersonDtos;

    public MissingPersonListDto(List<MissingPerson> missingPersonDtos) {
        this.missingPersonDtos = missingPersonDtos.stream().map(MissingPersonElementDto::new).collect(Collectors.toList());
    }
}
