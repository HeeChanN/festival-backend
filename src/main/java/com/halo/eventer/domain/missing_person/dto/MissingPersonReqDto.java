package com.halo.eventer.domain.missing_person.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissingPersonReqDto {
    private String name;
    private String age;
    private String gender;
    private String thumbnail;
    private String missingLocation;
    private String missingTime;
    private String content;
    private String parentName;
    private String parentNo;
    private String domainUrlName;
}
