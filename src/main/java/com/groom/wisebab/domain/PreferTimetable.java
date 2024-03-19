package com.groom.wisebab.domain;

import com.groom.wisebab.IntegerArrayConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class PreferTimetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prefer_timetable_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "promise_id")
    private Promise promise;

    @Convert(converter = IntegerArrayConverter.class)
    private List<Integer> preferTime;

    public PreferTimetable(Member member, Promise promise, List<Integer> preferTime) {
        this.member = member;
        this.promise = promise;
        this.preferTime = preferTime;
    }
}
