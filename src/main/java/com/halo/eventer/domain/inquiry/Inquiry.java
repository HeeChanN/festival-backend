package com.halo.eventer.domain.inquiry;

import java.time.LocalDateTime;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.inquiry.dto.InquiryCreateReqDto;
import com.halo.eventer.domain.inquiry.dto.InquiryUserReqDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", length = 500)
    private String content;

    @Column(name = "answer", length = 500)
    private String answer;

    @Column(name = "is_secret", nullable = false)
    private boolean isSecret;

    @Column(name = "is_answered", nullable = false)
    private boolean isAnswered;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "password", nullable = false)
    private String password;

    @CreatedDate
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Inquiry(Festival festival, InquiryCreateReqDto inquiryCreateReqDto, String password) {
        this.festival = festival;
        this.title = inquiryCreateReqDto.getTitle();
        this.isSecret = inquiryCreateReqDto.getIsSecret();
        this.userId = inquiryCreateReqDto.getUserId();
        this.content = inquiryCreateReqDto.getContent();
        this.isAnswered = false;
        this.password = password;
    }

    public void registerAnswer(String answer) {
        this.answer = answer;
        this.isAnswered = true;
    }

    public boolean isOwner(InquiryUserReqDto inquiryUserReqDto) {
        return this.userId.equals(inquiryUserReqDto.getUserId());
    }
}
