package com.halo.eventer.domain.inquiry.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class InquiryUserReqDto {

    @NotNull
    @Size(max = 16)
    private String userId;

    @NotNull
    @Pattern(regexp = "^[0-9]{4}$", message = "비밀번호는 숫자 4자리여야 합니다.")
    private String password;

    public InquiryUserReqDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
