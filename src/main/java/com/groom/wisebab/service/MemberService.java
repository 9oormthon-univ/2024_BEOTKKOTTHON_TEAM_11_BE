package com.groom.wisebab.service;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.dto.member.LoginRequest;
import com.groom.wisebab.dto.member.MemberResponseDTO;
import com.groom.wisebab.dto.member.SignUpDTO;
import com.groom.wisebab.jwt.JWTUtil;
import com.groom.wisebab.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public Long createMember(SignUpDTO signUpDTO) {

        Member member = new Member(signUpDTO.getUsername(), passwordEncoder.encode(signUpDTO.getPassword()), signUpDTO.getNickname(), "ROLE_USER");

        memberRepository.save(member);

        return member.getId();
    }

    /*
    @Transactional
    public String login(LoginRequest loginRequest) {
        jwtUtil.createJwt(loginRequest.getUsername(), loginRequest.getPassword(), )
    }
    */

    public Optional<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public MemberResponseDTO convertToDTO(Member member) {
        return new MemberResponseDTO(member.getId(), member.getUsername(), member.getNickname());
    }

    public Optional<Member> findMemberByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
