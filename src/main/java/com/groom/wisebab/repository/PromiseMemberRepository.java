package com.groom.wisebab.repository;

import com.groom.wisebab.domain.PromiseMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromiseMemberRepository extends JpaRepository<PromiseMember, Long> {
    List<PromiseMember> findAllByMemberId(Long memberId);

    List<PromiseMember> findAllByPromiseId(Long promiseId);
}
