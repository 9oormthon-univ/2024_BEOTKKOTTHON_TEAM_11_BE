package com.groom.wisebab.repository;

import com.groom.wisebab.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;
import com.groom.wisebab.domain.Promise;

import java.util.List;

public interface PromiseRepository extends JpaRepository<Promise, Long> {
    List<Promise> findAllByState(State state);
}
