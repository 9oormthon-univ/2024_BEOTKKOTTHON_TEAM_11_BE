package com.groom.wisebab.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    /**
        eamil을 username으로 사용.
     */
    private String username;

    private String password;

    private String nickname;

    private String role;

    //member이 연관관계 주인
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PromiseMember> promises = new ArrayList<>();

    public Member(String username, String password, String nickname, String role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

}
