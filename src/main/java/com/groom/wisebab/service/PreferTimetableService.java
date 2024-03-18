package com.groom.wisebab.service;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.domain.PreferTimetable;
import com.groom.wisebab.domain.Promise;
import com.groom.wisebab.repository.PreferTimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PreferTimetableService {

    private final PreferTimetableRepository preferTimetableRepository;

    public Long createPreferTimetable(Promise promise, Member member, List<Integer> preferTime) {
        PreferTimetable preferTimetable = new PreferTimetable(member, promise, preferTime);
        preferTimetableRepository.save(preferTimetable);

        return preferTimetable.getId();
    }
}
