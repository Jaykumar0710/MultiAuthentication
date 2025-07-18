package com.SecureVault.MultiAuthentication.repository;

import com.SecureVault.MultiAuthentication.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findTopByEmailOrderByCreatedAtDesc(String email);
}
