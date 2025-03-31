package com.halo.eventer.domain.stamp.dto.stamp;

import com.halo.eventer.domain.stamp.StampUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StampUsersGetDto {
  private String uuid;
  private String name;
  private String phone;
  private boolean finished;
  private int participantCount;

  public static List<StampUsersGetDto> fromEntities(List<StampUser> stampUsers){
    return stampUsers.stream()
            .map(StampUsersGetDto::from)
            .collect(Collectors.toList());
  }

  public static StampUsersGetDto from(StampUser stampUser){
    return new StampUsersGetDto(
            stampUser.getUuid(),
            stampUser.getName(),
            stampUser.getPhone(),
            stampUser.isFinished(),
            stampUser.getParticipantCount()
    );
  }
}
