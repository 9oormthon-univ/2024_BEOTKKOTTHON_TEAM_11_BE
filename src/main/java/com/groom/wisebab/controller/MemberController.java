package com.groom.wisebab.controller;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.dto.email.EmailCertifyCodeDTO;
import com.groom.wisebab.dto.email.EmailCertifyDTO;
import com.groom.wisebab.dto.member.LoginRequest;
import com.groom.wisebab.dto.member.MemberResponseDTO;
import com.groom.wisebab.dto.member.SignUpDTO;
import com.groom.wisebab.jwt.JWTUtil;
import com.groom.wisebab.service.MemberService;
import com.univcert.api.UnivCert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final String UnivCertKEY = "48ad7af1-31bc-4c1f-ad01-f06001a618da";

    @PostMapping("/signup")
    public Long signUp(@RequestBody SignUpDTO signUpDTO) {
        return memberService.createMember(signUpDTO);
    }

    /*
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate()
    }
    */

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

    @GetMapping("/members/username/{memberUsername}")
    public ResponseEntity<MemberResponseDTO> findMemberByUsername(@PathVariable String memberUsername) {
        Member member = memberService.findMemberByUsername(memberUsername);
        MemberResponseDTO memberResponseDTO = new MemberResponseDTO(member.getId(), member.getUsername(), member.getNickname());

        return ResponseEntity.ok(memberResponseDTO);
    }


    // UnivCert API를 통해 가입하려는 회원의 대학교 이메일로 인증 코드 전송
    @PostMapping("/verification")
    public Map<String, Object> emailVerification(@RequestBody EmailCertifyDTO emailCertifyDTO) throws IOException {
        return UnivCert.certify(UnivCertKEY, emailCertifyDTO.getEmail(), emailCertifyDTO.getUnivName(), true);
    }

    // 이메일로 보낸 인증코드와 사용자가 입력한 코드가 일치하는지 검증
    @PostMapping("/verificationCode")
    public Map<String, Object> emailCodeVerification(@RequestBody EmailCertifyCodeDTO emailCertifyCodeDTO) throws IOException {
        return UnivCert.certifyCode(UnivCertKEY, emailCertifyCodeDTO.getEmail(), emailCertifyCodeDTO.getUnivName(), emailCertifyCodeDTO.getCode());
    }


}
