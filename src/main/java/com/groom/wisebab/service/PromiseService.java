package com.groom.wisebab.service;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.domain.Promise;
import com.groom.wisebab.domain.PromiseMember;
import com.groom.wisebab.domain.State;
import com.groom.wisebab.dto.promise.*;
import com.groom.wisebab.repository.MemberRepository;
import com.groom.wisebab.repository.PreferTimetableRepository;
import com.groom.wisebab.repository.PromiseMemberRepository;
import com.groom.wisebab.repository.PromiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromiseService {

    private final PromiseRepository promiseRepository;
    private final MemberRepository memberRepository;
    private final PromiseMemberRepository promiseMemberRepository;
    private final PreferTimetableRepository preferTimetableRepository;

    // 약속 생성
    @Transactional
    public Long createPromise(PromiseDTO promiseDTO) {
        Member owner = memberRepository.findById(promiseDTO.getOwnerId())
                .orElseThrow(
                        NullPointerException::new
                );
        Promise promise = new Promise(promiseDTO.getTitle(), owner.getId(), promiseDTO.getLocName(), promiseDTO.getLocAddress(), promiseDTO.getStartDate(), promiseDTO.getEndDate(), promiseDTO.getMemo());
        promiseRepository.save(promise);

        PromiseMember promiseMember = new PromiseMember(promise, owner);
        owner.getPromises().add(promiseMember);
        promise.getMemberList().add(promiseMember);

        promiseMemberRepository.save(promiseMember);

        return promise.getId();
    }

    // PromiseId를 통해 약속 조회
    public Optional<Promise> findPromiseById(Long id) {
        return promiseRepository.findById(id);
    }

    // 약속 리스트 DTO로 변환
    public List<PromiseDetailResponseDTO> converToDTOList(List<Promise> promises, Long memberId) {
        return promises.stream()
                .map(promise ->
                        convertToDTO(promise.getId(), memberId)
                )
                .collect(Collectors.toList());
    }

    // 약속 상세정보 DTO로 변환
    public PromiseDetailResponseDTO convertToDTO(Long promiseId, Long memberId) {
        Promise promise = promiseRepository.findById(promiseId)
                .orElseThrow(
                        NullPointerException::new
                );

        List<PromiseMembersInnerResponseDTO> promiseMembersInnerResponseDTOS = promise.getMemberList().stream()
                .map(promiseMember -> new PromiseMembersInnerResponseDTO(promiseMember.getMember().getId(), promiseMember.getMember().getNickname()))
                .collect(Collectors.toList());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(
                        NullPointerException::new
                );

        boolean isLeader = member.getId().equals(promise.getOwnerId());

        int confirmedPeopleCount = preferTimetableRepository.findAllByPromise(promise).size();

        boolean allResponded = confirmedPeopleCount == promise.getMemberList().size();

        return new PromiseDetailResponseDTO(promise.getId(), promise.getState(), promise.getTitle(), promise.getOwnerId(), isLeader, promise.getConfirmedDate(), promise.getConfirmedTime(), promise.getLocName(), promise.getLocAddress(), promise.getStartDate(), promise.getEndDate(), promise.getMemo(), promiseMembersInnerResponseDTOS, confirmedPeopleCount, allResponded);
    }

    // 대기중인 약속 -> 확정된 약속으로
    @Transactional
    public Long changeStatusToConfirmed(Long promiseId, UpdateToConfirmedDTO updateToConfirmedDTO) {
        Promise promise = promiseRepository.findById(promiseId)
                .orElseThrow(
                        NullPointerException::new
                );

        promise.updateToConfirmed(updateToConfirmedDTO.getConfirmedDate(), updateToConfirmedDTO.getConfirmedTime());
        return promise.getId();
    }

    // 확정된 약속 -> 만료된 약속
    @Transactional
    public Long changeStatusToExpired(Long promiseId, UpdateToExpiredDTO updateToExpiredDTO) {
        Promise promise = promiseRepository.findById(promiseId)
                .orElseThrow(
                        NullPointerException::new
                );

        promise.updateToExpired(updateToExpiredDTO.getBankAccount(), updateToExpiredDTO.getKakaopayLink(), updateToExpiredDTO.getPayMemo());
        return promise.getId();
    }

    // 초대링크(UUID)로 들어오는 회원을 참가시키는 메서드
    @Transactional
    public Long memberParticipation(Member member, UUID uuid) {
        Promise promise = promiseRepository.findPromiseByUuid(uuid);

        PromiseMember promiseMember = new PromiseMember(promise, member);
        promise.getMemberList().add(promiseMember);
        PromiseMember savedPromiseMember = promiseMemberRepository.save(promiseMember);

        return savedPromiseMember.getPromise().getId();
    }

    // 송금 정보 조회
    public PromisePaymentResponseDTO getPaymentInfo(Long promiseId) {
        Promise promise = promiseRepository.findById(promiseId)
                .orElseThrow(
                        NullPointerException::new
                );
        return new PromisePaymentResponseDTO(promise.getPayMemo(), promise.getKakaopayLink(), promise.getBankAccount());
    }

    public Promise findPromiseByUuid(UUID uuid) {
        return promiseRepository.findPromiseByUuid(uuid);
    }

}
