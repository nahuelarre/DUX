package com.challenge.dux.repository;

import com.challenge.dux.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByNameContainingIgnoreCase(String name);
    Optional<Team> findByNameIgnoreCase(String name);
}

