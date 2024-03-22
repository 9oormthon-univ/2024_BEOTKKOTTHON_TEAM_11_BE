package com.groom.wisebab.repository;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.domain.PreferTimetable;
import com.groom.wisebab.domain.Promise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PreferTimetableRepository extends JpaRepository<PreferTimetable, Long> {

     PreferTimetable findPreferTimeTableByPromiseAndMember(Promise promise, Member member);

}
