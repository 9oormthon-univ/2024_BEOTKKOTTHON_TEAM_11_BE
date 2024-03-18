package com.groom.wisebab.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Promise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promise_id")
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member owner;

    //promise가 연관관계 주인
    @OneToMany(mappedBy = "promise")
    private List<PromiseMember> memberList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private State state;

    private String locName;

    private String locAddress;

    //컬렉션 객체로 저장
    @ElementCollection
    @CollectionTable(name = "confirmed_time", joinColumns = @JoinColumn(name = "promise_id"))
    private List<Integer> confirmedTime = new ArrayList<>();

    private int totalPrice;

    private String bankAccount;

    public Promise(String title, Member owner, State state, String locName, String locAddress, int totalPrice, String bankAccount) {
        this.title = title;
        this.owner = owner;
        this.state = state;
        this.locName = locName;
        this.locAddress = locAddress;
        this.totalPrice = totalPrice;
        this.bankAccount = bankAccount;
        this.createDate = LocalDateTime.now();
    }

    private LocalDateTime createDate;
}
