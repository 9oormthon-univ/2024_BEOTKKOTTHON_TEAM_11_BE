package com.groom.wisebab.repository;

import com.groom.wisebab.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;
import com.groom.wisebab.domain.Promise;

import java.util.List;
import java.util.UUID;

public interface PromiseRepository extends JpaRepository<Promise, Long> {
    List<Promise> findAllByState(State state);

    Promise findPromiseByUuid(UUID uuid);
}
