package com.groom.wisebab.service;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.dto.member.CustomUserDetails;
import com.groom.wisebab.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> userData = memberRepository.findByUsername(username);

        return userData.map(CustomUserDetails::new).orElse(null);

    }
}
