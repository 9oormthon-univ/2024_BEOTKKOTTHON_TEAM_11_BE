package com.groom.wisebab.controller;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.dto.SignUpDTO;
import com.groom.wisebab.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/v1/signup")
    public String signUp(SignUpDTO signUpDTO) {
        memberService.createMember(signUpDTO);
        return "ok";
    }

    @PostMapping("/api/v1/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "ok";
    }

    @GetMapping("/api/v1/member/{memberId}")
    public Optional<Member> findMemberById(@PathVariable Long memberId) {
        return memberService.findMemberById(memberId);
    }



    @GetMapping("/")
    public String mainP() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        return "Main Controller" + username + role;
    }

}
