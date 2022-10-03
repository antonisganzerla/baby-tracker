package com.sgztech.domain.repository;

import com.sgztech.domain.entity.PasswordRecovery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, Integer> {

    Optional<PasswordRecovery> findByEmailAndCodeAndIsUsed(String email, String code, boolean isUsed);
}