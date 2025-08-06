package com.halo.eventer.domain.stamp.fixture;

import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.dto.stampUser.LoginDto;
import com.halo.eventer.domain.stamp.dto.stampUser.SignupDto;
import com.halo.eventer.domain.stamp.dto.stampUser.SignupWithCustomDto;
import com.halo.eventer.domain.stamp.dto.stampUser.SignupWithoutCustomDto;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class StampUserFixture {

    public static final String 암호화된_번호 = "encrypted phone";
    public static final String 암호화된_이름 = "encrypted name";

    public static final Long 스탬프유저1 = 1L;
    public static final Long 스탬프유저2 = 2L;
    public static final Long 스탬프유저3 = 3L;
    public static final Long 스탬프유저4 = 4L;
    public static final Long 스탬프유저5 = 5L;
    public static final Long 스탬프유저6 = 6L;
    public static final Long 스탬프유저7 = 7L;

    public static StampUser 스탬프유저1_생성() {
        return new StampUser(암호화된_번호, 암호화된_이름, 1);
    }

    public static SignupDto 회원가입_DTO_생성() {
        SignupDto signupDto = new SignupDto();
        setField(signupDto, "name", "test name");
        setField(signupDto, "phone", "test phone");
        setField(signupDto, "participantCount", 10);
        return signupDto;
    }

    public static SignupWithoutCustomDto 커스텀_없는_회원가입_DTO_생성() {
        SignupWithoutCustomDto signupDto = new SignupWithoutCustomDto();
        setField(signupDto, "name", "test name");
        setField(signupDto, "phone", "test phone");
        setField(signupDto, "participantCount", 10);
        return signupDto;
    }

    public static SignupWithCustomDto 커스텀_있는_회원가입_DTO_생성() {
        SignupWithCustomDto signupDto = new SignupWithCustomDto();
        setField(signupDto, "name", "test name");
        setField(signupDto, "phone", "test phone");
        setField(signupDto, "participantCount", 10);
        setField(signupDto, "schoolNo", "test school no");
        return signupDto;
    }

    public static LoginDto 로그인_DTO_생성() {
        LoginDto loginDto = new LoginDto();
        setField(loginDto, "name", "test");
        setField(loginDto, "phone", "01012341234");
        return loginDto;
    }
}
