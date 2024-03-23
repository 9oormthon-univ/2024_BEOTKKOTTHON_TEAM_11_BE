package com.groom.wisebab.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Promise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promise_id")
    private Long id;

    private String title;
/*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member owner;
 */
    private Long ownerId;

    //promise가 연관관계 주인
    @OneToMany(mappedBy = "promise", cascade = CascadeType.ALL)
    private List<PromiseMember> memberList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private State state;

    private String locName;

    private String locAddress;

    /*
    //컬렉션 객체로 저장
    @ElementCollection
    @CollectionTable(name = "confirmed_time", joinColumns = @JoinColumn(name = "promise_id"))
    private List<Integer> confirmedTime = new ArrayList<>();
     */

    private LocalDate confirmedDate;

    private String confirmedTime;

    private String bankAccount;

    private String kakaopayLink;

    private String payMemo;

    private LocalDate startDate;

    private String memo;

    @Column(columnDefinition = "BINARY(16)")
    private UUID uuid;

    public Promise(String title, Long ownerId, String locName, String locAddress, LocalDate startDate, String memo) {
        this.title = title;
        this.ownerId = ownerId;
        this.state = State.PENDING;
        this.locName = locName;
        this.locAddress = locAddress;
        this.startDate = startDate;
        this.memo = memo;
        this.uuid = UUID.randomUUID();
    }

    public void updateToConfirmed(LocalDate confirmedDate, String confirmedTime) {
        this.state = State.CONFIRMED;
        this.confirmedDate = confirmedDate;
        this.confirmedTime = confirmedTime;
    }

    public void updateToExpired(String bankAccount, String kakaopayLink, String payMemo) {
        this.bankAccount = bankAccount;
        this.kakaopayLink = kakaopayLink;
        this.payMemo = payMemo;
    }

}
