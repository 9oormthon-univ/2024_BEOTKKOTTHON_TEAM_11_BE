package com.groom.wisebab.controller;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.dto.email.EmailCertifyCodeDTO;
import com.groom.wisebab.dto.email.EmailCertifyDTO;
import com.groom.wisebab.dto.member.MemberResponseDTO;
import com.groom.wisebab.dto.member.SignUpDTO;
import com.groom.wisebab.service.MemberService;
import com.univcert.api.UnivCert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final String UnivCertKEY = "dfafd059-19f9-4553-ac2a-07f9d498739a";

    @PostMapping("/signup")
    public Long signUp(SignUpDTO signUpDTO) {
        return memberService.createMember(signUpDTO);
    }

    @PostMapping("/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "ok";
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<MemberResponseDTO> findMemberById(@PathVariable Long memberId) {
        Member member = memberService.findMemberById(memberId)
                .orElseThrow(
                        NullPointerException::new
                );
        MemberResponseDTO memberResponseDTO = memberService.convertToDTO(member);
        return ResponseEntity.ok(memberResponseDTO);
    }


    @PostMapping("/verification")
    public Map<String, Object> emailVerification(@RequestBody EmailCertifyDTO emailCertifyDTO) throws IOException {
        return UnivCert.certify(UnivCertKEY, emailCertifyDTO.getEmail(), emailCertifyDTO.getUnivName(), true);
    }

    @PostMapping("/verificationCode")
    public Map<String, Object> emailCodeVerification(@RequestBody EmailCertifyCodeDTO emailCertifyCodeDTO) throws IOException {
        return UnivCert.certifyCode(UnivCertKEY, emailCertifyCodeDTO.getEmail(), emailCertifyCodeDTO.getUnivName(), emailCertifyCodeDTO.getCode());
    }


}
