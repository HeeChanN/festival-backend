package com.halo.eventer.domain.member;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Authority> authorities;

    public void setMember(List<Authority> roles) {
        this.authorities = roles;
        roles.forEach(o -> o.setMember(this));
    }

    public Member(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public void setRoles(List<Authority> roles) {
        this.authorities = roles;
        roles.forEach(o -> o.setMember(this));
    }

    public List<String> getRoleNames() {
        return authorities.stream().map(Authority::getRoleName).collect(Collectors.toList());
    }
}
