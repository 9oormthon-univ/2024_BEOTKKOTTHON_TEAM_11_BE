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
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class PreferTimetableController {

    private final PreferTimetableService preferTimetableService;
    private final MemberService memberService;
    private final PromiseService promiseService;

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
        log.info("리턴 직전");
        return ResponseEntity.ok(preferTimetableDTOS);
    }
}
