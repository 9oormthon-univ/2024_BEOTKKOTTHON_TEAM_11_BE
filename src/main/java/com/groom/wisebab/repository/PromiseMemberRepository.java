package com.groom.wisebab.repository;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.domain.Promise;
import com.groom.wisebab.domain.PromiseMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PromiseMemberRepository extends JpaRepository<PromiseMember, Long> {
    List<PromiseMember> findAllByMemberId(Long memberId);

    Optional<PromiseMember> findPromiseMemberByMemberAndPromise(Member member, Promise promise);

    List<PromiseMember> findAllByPromiseId(Long promiseId);
}
