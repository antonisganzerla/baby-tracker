package com.sgztech.domain.repository;

import com.sgztech.domain.entity.Baby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BabyRepository extends JpaRepository<Baby, Integer> {
}