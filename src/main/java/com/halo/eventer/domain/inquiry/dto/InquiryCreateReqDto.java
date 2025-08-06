package com.halo.eventer.domain.inquiry.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class InquiryCreateReqDto {

    @NotNull
    private String title;

    @NotNull
    private Boolean isSecret;

    @NotNull
    @Size(min = 1, max = 16)
    private String userId;

    @NotNull
    @Pattern(regexp = "^[0-9]{4}$", message = "비밀번호는 숫자 4자리여야 합니다.")
    private String password;

    @NotNull
    @Size(max = 500)
    private String content;

    public InquiryCreateReqDto(String title, Boolean isSecret, String userId, String password, String content) {
        this.title = title;
        this.isSecret = isSecret;
        this.userId = userId;
        this.password = password;
        this.content = content;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
