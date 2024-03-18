package com.groom.wisebab.service;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.domain.Promise;
import com.groom.wisebab.domain.State;
import com.groom.wisebab.repository.MemberRepository;
import com.groom.wisebab.repository.PromiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromiseService {

    private final PromiseRepository promiseRepository;
    private final MemberRepository memberRepository;

    public Long createPromise(String title, Member owner, State state, String locName, String locAddress, int totalPrice, String bankAccount) {
        Promise promise = new Promise(title, owner, state, locName, locAddress, totalPrice, bankAccount);
        promiseRepository.save(promise);

        return promise.getId();
    }

    public Optional<Promise> findPromiseById(Long id) {
        return promiseRepository.findById(id);
    }

    public List<Promise> findAllPromiseByState(State state) {
        return promiseRepository.findAllByState(state);
    }

}
