package com.groom.wisebab.service;

import com.groom.wisebab.domain.Promise;
import com.groom.wisebab.domain.PromiseMember;
import com.groom.wisebab.domain.State;
import com.groom.wisebab.repository.PromiseMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromiseMemberService {

    private final PromiseMemberRepository promiseMemberRepository;


    // memberId로 promiseMember 배열을 반환
    public List<PromiseMember> findAllByMemberId(Long memberId) {
        return promiseMemberRepository.findAllByMemberId(memberId);
    }

    // 회원이 참가하는 약속들을 상태에 따라 promise 배열로 반환
    public List<Promise> findAllByMemberIdAndState(Long memberId, State state) {
        List<PromiseMember> findPromise = findAllByMemberId(memberId);
        return findPromise.stream()
                .map(PromiseMember::getPromise)
                .filter(promise -> promise.getState() == state)
                .toList();
    }
}
