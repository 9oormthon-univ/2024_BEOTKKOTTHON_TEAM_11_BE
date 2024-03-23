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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO) {
        if (memberService.findMemberByUsername(signUpDTO.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
        }

        Long memberId = memberService.createMember(signUpDTO);
        return ResponseEntity.ok(memberId);
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

    @GetMapping("/memberByUsername/{memberUsername}")
    public ResponseEntity<MemberResponseDTO> findMemberByUsername(@PathVariable String memberUsername) {
        Member member = memberService.findMemberByUsername(memberUsername)
                .orElseThrow(
                        NullPointerException::new
                );
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
        Map<String, Object> result = UnivCert.certifyCode(UnivCertKEY, emailCertifyCodeDTO.getEmail(), emailCertifyCodeDTO.getUnivName(), emailCertifyCodeDTO.getCode());

        log.info("인증된 이메일인지 체크");
        UnivCert.status(UnivCertKEY, emailCertifyCodeDTO.getEmail());

        log.info("인증 유저 리스트");
        UnivCert.list(UnivCertKEY);

        log.info("초기화 시작");
        UnivCert.clear(UnivCertKEY);

        return result;
    }



}
