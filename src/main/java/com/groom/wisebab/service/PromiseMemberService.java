package com.groom.wisebab.service;

import com.groom.wisebab.domain.Promise;
import com.groom.wisebab.domain.PromiseMember;
import com.groom.wisebab.domain.State;
import com.groom.wisebab.repository.PromiseMemberRepository;
import com.groom.wisebab.repository.PromiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromiseMemberService {

    private final PromiseMemberRepository promiseMemberRepository;


    public List<PromiseMember> findAllByMemberId(Long memberId) {
        return promiseMemberRepository.findAllByMemberId(memberId);
    }

    public List<Promise> findAllByMemberIdAndState(Long memberId, State state) {
        List<PromiseMember> findPromise = findAllByMemberId(memberId);
        return findPromise.stream()
                .map(PromiseMember::getPromise)
                .filter(promise -> promise.getState() == state)
                .toList();
    }
}
