package com.groom.wisebab.controller;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.domain.Promise;
import com.groom.wisebab.domain.State;
import com.groom.wisebab.dto.promise.*;
import com.groom.wisebab.service.MemberService;
import com.groom.wisebab.service.PromiseMemberService;
import com.groom.wisebab.service.PromiseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class PromiseController {

    private final PromiseService promiseService;
    private final MemberService memberService;
    private final PromiseMemberService promiseMemberService;

    @PostMapping("/promises")
    public Long createPromise(@RequestBody PromiseDTO promiseDTO) {
        return promiseService.createPromise(promiseDTO);
    }

    // 약속 단건 조회 {상태에 따라 pending일때와 confirmed, expired 일때}
    @GetMapping("/promises/{promiseId}/details/members/{memberId}")
    public ResponseEntity<PromiseDetailResponseDTO> findPromiseById(@PathVariable Long promiseId, @PathVariable Long memberId) {

        PromiseDetailResponseDTO promiseDetailResponseDTO = promiseService.convertToDTO(promiseId, memberId);
        return ResponseEntity.ok(promiseDetailResponseDTO);
    }

    // 상태에 따른 약속 리스트 조회
    @GetMapping("/members/{memberId}/promises")
    public ResponseEntity<List<PromiseDetailResponseDTO>> findAllPromiseByMemberAndState(@PathVariable Long memberId, @RequestParam State state) {

        List<Promise> promises = promiseMemberService.findAllByMemberIdAndState(memberId, state);
        List<PromiseDetailResponseDTO> promiseDetailResponseDTOList = promiseService.converToDTOList(promises, memberId);

        return ResponseEntity.ok(promiseDetailResponseDTOList);
    }

    // 대기중인 약속 -> 확정된 약속
    @PatchMapping("/promises/{promiseId}/confirmation")
    public Long updateToConfirmed(@PathVariable Long promiseId, @RequestBody UpdateToConfirmedDTO updateToConfirmedDTO) {
        return promiseService.changeStatusToConfirmed(promiseId, updateToConfirmedDTO);
    }

    // 확정된 약속 -> 만료된 약속
    @PatchMapping("/promises/{promiseId}/termination")
    public Long updateToExpired(@PathVariable Long promiseId, @RequestBody UpdateToExpiredDTO updateToExpiredDTO) {
        return promiseService.changeStatusToExpired(promiseId, updateToExpiredDTO);
    }

    // 약속의 초대 링크 조회
    @GetMapping("/promises/{promiseId}/link")
    public ResponseEntity<PromiseLinkDTO> getPromiseLink(@PathVariable Long promiseId) {
        Promise promise = promiseService.findPromiseById(promiseId)
                .orElseThrow(
                        NullPointerException::new
                );
        UUID uuid = promise.getUuid();


        PromiseLinkDTO promiseLinkDTO = new PromiseLinkDTO(uuid.toString());
        return ResponseEntity.ok(promiseLinkDTO);
    }

    // uuid로 이루어진 초대링크를 접속하면 그 회원을 약속 파티원으로 추가
    @PostMapping("/members/{memberId}/promises/{uuid}")
    public ResponseEntity<?> memberParticipation(@PathVariable Long memberId, @PathVariable UUID uuid) {
        Member member = memberService.findMemberById(memberId)
                .orElseThrow(
                        NullPointerException::new
                );
        Promise promise = promiseService.findPromiseByUuid(uuid);

        if (promiseMemberService.findPromiseMemberByMemberAndPromise(member, promise).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 참가한 회원입니다.");
        }

        Long promiseId = promiseService.memberParticipation(member, uuid);

        return ResponseEntity.ok(promiseId);
    }

    // 약속의 송금 정보 조회
    @GetMapping("/promises/{promiseId}/payment")
    public ResponseEntity<PromisePaymentResponseDTO> getPaymentInfo(@PathVariable Long promiseId) {
        PromisePaymentResponseDTO promisePaymentResponseDTO = promiseService.getPaymentInfo(promiseId);

        return ResponseEntity.ok(promisePaymentResponseDTO);
    }

}
