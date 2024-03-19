package com.groom.wisebab.service;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.dto.SignUpDTO;
import com.groom.wisebab.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long createMember(SignUpDTO signUpDTO) {

        Member member = new Member(signUpDTO.getUsername(), passwordEncoder.encode(signUpDTO.getPassword()), signUpDTO.getNickname(), "ROLE_USER");

        memberRepository.save(member);

        return member.getId();
    }



    public Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Optional<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }


}
