package com.groom.wisebab.service;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.repository.MemberRepository;
import com.groom.wisebab.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    public Long createMember(String password, String nickname, String email) {
        String encodedPassword = passwordEncoder.encode(password);
        Member member = new Member(encodedPassword, nickname, email);
        memberRepository.save(member);

        return member.getId();
    }

    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email);
        if (member == null || !passwordEncoder.matches(password, member.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        return jwtTokenUtil.generateAccessToken(member.getId());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Optional<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }


}
