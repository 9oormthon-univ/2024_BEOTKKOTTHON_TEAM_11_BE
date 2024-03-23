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

    private Long ownerId;

    //promise가 연관관계 주인
    @OneToMany(mappedBy = "promise", cascade = CascadeType.ALL)
    private List<PromiseMember> memberList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private State state;

    private String locName;

    private String locAddress;

    private LocalDate confirmedDate;

    private String confirmedTime;

    private String bankAccount;

    private String kakaopayLink;

    private String payMemo;

    private LocalDate startDate;

    private LocalDate endDate;

    private String memo;

    @Column(columnDefinition = "BINARY(16)")
    private UUID uuid;

    //생성자
    public Promise(String title, Long ownerId, String locName, String locAddress, LocalDate startDate, LocalDate endDate, String memo) {
        this.title = title;
        this.ownerId = ownerId;
        this.state = State.PENDING;
        this.locName = locName;
        this.locAddress = locAddress;
        this.startDate = startDate;
        this.endDate = endDate;
        this.memo = memo;
        this.uuid = UUID.randomUUID();
    }

    //대기중인 약속 -> 확정된 약속으로 변경하는 메서드
    public void updateToConfirmed(LocalDate confirmedDate, String confirmedTime) {
        this.state = State.CONFIRMED;
        this.confirmedDate = confirmedDate;
        this.confirmedTime = confirmedTime;
    }

    //확정된 약속 -> 만료된 약속으로 변경하는 메서드
    public void updateToExpired(String bankAccount, String kakaopayLink, String payMemo) {
        this.state = State.EXPIRED;
        this.bankAccount = bankAccount;
        this.kakaopayLink = kakaopayLink;
        this.payMemo = payMemo;
    }

}
