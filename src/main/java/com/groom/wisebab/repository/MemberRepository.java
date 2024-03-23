package com.groom.wisebab.repository;

import com.groom.wisebab.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);

    Member findByNickname(String nickname);
}
