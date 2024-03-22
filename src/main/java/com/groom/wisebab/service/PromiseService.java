package com.groom.wisebab.service;

import com.groom.wisebab.domain.Member;
import com.groom.wisebab.domain.Promise;
import com.groom.wisebab.domain.PromiseMember;
import com.groom.wisebab.domain.State;
import com.groom.wisebab.dto.promise.*;
import com.groom.wisebab.repository.MemberRepository;
import com.groom.wisebab.repository.PromiseMemberRepository;
import com.groom.wisebab.repository.PromiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional
    public Long createPromise(PromiseDTO promiseDTO) {
        Member owner = memberRepository.findById(promiseDTO.getOwnerId())
                .orElseThrow(
                        NullPointerException::new
                );
        Promise promise = new Promise(promiseDTO.getTitle(), owner.getNickname(), promiseDTO.getLocName(), promiseDTO.getLocAddress(), promiseDTO.getStartDate(), promiseDTO.getMemo());
        promiseRepository.save(promise);

        PromiseMember promiseMember = new PromiseMember(promise, owner);
        owner.getPromises().add(promiseMember);
        promise.getMemberList().add(promiseMember);

        promiseMemberRepository.save(promiseMember);

        return promise.getId();
    }

    public Optional<Promise> findPromiseById(Long id) {
        return promiseRepository.findById(id);
    }

    public List<Promise> findAllPromiseByState(State state) {
        return promiseRepository.findAllByState(state);
    }

    public List<PromiseListResponseDTO> converToDTOList(List<Promise> promises) {
        return promises.stream()
                .map(promise -> new PromiseListResponseDTO(
                        promise.getId(),
                        promise.getTitle(),
                        promise.getState(),
                        promise.getLocName(),
                        promise.getOwnerName(),
                        promise.getConfirmedDate(),
                        promise.getConfirmedTime(),
                        promise.getMemberList().stream()
                                .map(promiseMember -> new PromiseMembersInnerResponseDTO(promiseMember.getMember().getId(), promiseMember.getMember().getNickname()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public PromiseDetailResponseDTO convertToDTO(Promise promise) {
        List<PromiseMembersInnerResponseDTO> promiseMembersInnerResponseDTOS = promise.getMemberList().stream()
                .map(promiseMember -> new PromiseMembersInnerResponseDTO(promiseMember.getMember().getId(), promiseMember.getMember().getNickname()))
                .collect(Collectors.toList());
        return new PromiseDetailResponseDTO(promise.getId(), promise.getState(), promise.getTitle(), promise.getOwnerName(), promise.getConfirmedDate(), promise.getConfirmedTime(), promise.getLocName(), promise.getLocAddress(), promise.getMemo(), promiseMembersInnerResponseDTOS);
    }

    @Transactional
    public Long changeStatusToConfirmed(Long promiseId, UpdateToConfirmedDTO updateToConfirmedDTO) {
        Promise promise = promiseRepository.findById(promiseId)
                .orElseThrow(
                        NullPointerException::new
                );

        promise.updateToConfirmed(updateToConfirmedDTO.getConfirmedDate(), updateToConfirmedDTO.getConfirmedTime());
        return promise.getId();
    }

    @Transactional
    public Long changeStatusToExpired(Long promiseId, UpdateToExpiredDTO updateToExpiredDTO) {
        Promise promise = promiseRepository.findById(promiseId)
                .orElseThrow(
                        NullPointerException::new
                );

        promise.updateToExpired(updateToExpiredDTO.getBankAccount(), updateToExpiredDTO.getKakaopayLink(), updateToExpiredDTO.getPayMemo());
        return promise.getId();
    }

    @Transactional
    public Long memberParticipation(Member member, UUID uuid) {
        Promise promise = promiseRepository.findPromiseByUuid(uuid);

        PromiseMember promiseMember = new PromiseMember(promise, member);
        promise.getMemberList().add(promiseMember);
        PromiseMember savedPromiseMember = promiseMemberRepository.save(promiseMember);

        return savedPromiseMember.getId();
    }
}
