package com.halo.eventer.domain.stamp.dto.stampUser.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampUserInfoResDto {
    private Long id;
    private String name;
    private String phone;
    private String uuid;
    private Boolean finished;
    private Integer participantCount;
    private String receivedPrizeName;
    private LocalDateTime createdAt;
}
