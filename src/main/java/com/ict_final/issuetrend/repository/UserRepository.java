package com.ict_final.issuetrend.repository;

import com.ict_final.issuetrend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    Optional<User> findByRefreshToken(String refreshToken);
}
