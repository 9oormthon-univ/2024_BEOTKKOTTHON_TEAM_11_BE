package com.groom.wisebab.service;

import com.groom.wisebab.repository.PromiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromiseMemberService {

    private final PromiseRepository promiseRepository;

}
