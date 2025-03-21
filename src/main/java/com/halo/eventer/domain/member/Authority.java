package com.halo.eventer.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roleName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    @Setter
    @JsonIgnore
    private Member member;

    @Builder
    public Authority(String roleName) {
        this.roleName = roleName;
    }

}
