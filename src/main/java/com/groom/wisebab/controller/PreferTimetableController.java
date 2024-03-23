package com.groom.wisebab.controller;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.domain.Promise;
import com.groom.wisebab.dto.PreferTimetableDTO;
import com.groom.wisebab.service.MemberService;
import com.groom.wisebab.service.PreferTimetableService;
import com.groom.wisebab.service.PromiseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class PreferTimetableController {

    private final PreferTimetableService preferTimetableService;
    private final MemberService memberService;
    private final PromiseService promiseService;

    // 약속 선호 시간 생성
    @PostMapping("/promises/{promiseId}/members/{memberId}")
    public Long createPreferTimetable(@PathVariable Long promiseId, @PathVariable Long memberId, @RequestBody List<PreferTimetableDTO> preferTimetableDTOS) {
        Promise promise = promiseService.findPromiseById(promiseId)
                .orElseThrow(
                        NullPointerException::new
                );
        Member member = memberService.findMemberById(memberId)
                .orElseThrow(
                        NullPointerException::new
                );

        return preferTimetableService.createPreferTimetable(promise, member, preferTimetableDTOS);
    }

    // 약속에서 해당 회원이 제출한 선호 시간표 조회
    @GetMapping("/promises/{promiseId}/members/{memberId}")
    public ResponseEntity<List<PreferTimetableDTO>> findTimetableByPromiseAndMember(@PathVariable Long promiseId, @PathVariable Long memberId) {
        log.info("컨트롤러 진입");
        Promise promise = promiseService.findPromiseById(promiseId)
                .orElseThrow(
                        NullPointerException::new
                );
        log.info("약속 조회 완료");
        Member member = memberService.findMemberById(memberId)
                .orElseThrow(
                        NullPointerException::new
                );
        log.info("회원 조회 완료");
        List<PreferTimetableDTO> preferTimetableDTOS = preferTimetableService.findTimetableByPromiseAndMember(promise, member);
        log.info("DTO에 담기 성공");
        return ResponseEntity.ok(preferTimetableDTOS);
    }
}
