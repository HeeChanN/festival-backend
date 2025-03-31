package com.halo.eventer.domain.missing_person.dto;

import com.halo.eventer.domain.missing_person.MissingPerson;
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

  public MissingPersonReqDto(MissingPerson m) {
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
}
