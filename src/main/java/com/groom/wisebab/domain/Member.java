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

    private String password;

    private String nickname;

    private String email;

    //member이 연관관계 주인
    @OneToMany(mappedBy = "member")
    private List<PromiseMember> promises = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Promise> ownerPromises = new ArrayList<>();

    public Member(String password, String nickname, String email) {
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }
}
