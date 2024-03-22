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
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    @GetMapping("/promises/{promiseId}")
    public ResponseEntity<PromiseDetailResponseDTO> findPromiseById(@PathVariable Long promiseId) {

        Promise promise = promiseService.findPromiseById(promiseId)
                .orElseThrow(
                        NullPointerException::new
                );


        PromiseDetailResponseDTO promiseDetailResponseDTO = promiseService.convertToDTO(promise);
        return ResponseEntity.ok(promiseDetailResponseDTO);
    }

    // 상태에 따른 약속 리스트 조회
    @GetMapping("/members/{memberId}/promises")
    public ResponseEntity<List<PromiseListResponseDTO>> findAllPromiseByMemberAndState(@PathVariable Long memberId, @RequestParam State state) {

        List<Promise> promises = promiseMemberService.findAllByMemberIdAndState(memberId, state);
        List<PromiseListResponseDTO> promiseListResponseDTOList = promiseService.converToDTOList(promises);

        return ResponseEntity.ok(promiseListResponseDTOList);
    }

    @PatchMapping("/promises/{promiseId}/confirmation")
    public Long updateToConfirmed(@PathVariable Long promiseId, @RequestBody UpdateToConfirmedDTO updateToConfirmedDTO) {
        return promiseService.changeStatusToConfirmed(promiseId, updateToConfirmedDTO);
    }

    @PatchMapping("/promises/{promiseId}/termination")
    public Long updateToExpired(@PathVariable Long promiseId, @RequestBody UpdateToExpiredDTO updateToExpiredDTO) {
        return promiseService.changeStatusToExpired(promiseId, updateToExpiredDTO);
    }

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

    @PostMapping("/members/{memberId}/promises/{uuid}")
    public Long memberParticipation(@PathVariable Long memberId, @PathVariable UUID uuid) {
        Member member = memberService.findMemberById(memberId)
                .orElseThrow(
                        NullPointerException::new
                );
        return promiseService.memberParticipation(member, uuid);
    }

}
