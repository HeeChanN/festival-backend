package com.halo.eventer.domain.stamp.dto.stampUser.response;

import java.time.LocalDateTime;
import java.util.List;

import com.halo.eventer.domain.stamp.StampUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampUserSummaryResDto {
    private Long id;
    private String name;
    private String phone;
    private String uuid;
    private Boolean finished;
    private LocalDateTime createdAt;

    public static StampUserSummaryResDto from(StampUser stampUser) {
        return new StampUserSummaryResDto(
                stampUser.getId(),
                stampUser.getName(),
                stampUser.getPhone(),
                stampUser.getUuid(),
                stampUser.getFinished(),
                stampUser.getCreatedAt());
    }

    public static StampUserSummaryResDto from(
            long id, String name, String phone, String uuid, boolean finished, LocalDateTime createdAt) {
        return new StampUserSummaryResDto(id, name, phone, uuid, finished, createdAt);
    }

    public static List<StampUserSummaryResDto> fromEntities(List<StampUser> stampUsers) {
        return stampUsers.stream().map(StampUserSummaryResDto::from).toList();
    }
}
