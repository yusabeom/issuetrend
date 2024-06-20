package com.ict_final.issuetrend.repository;

import com.ict_final.issuetrend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findByEmail(String email);

    Optional<User> findByRefreshToken(String refreshToken);

    @Query("SELECT u FROM User u WHERE u.userNo = :userNo")
    Optional<User> findByUserNo(@Param("userNo") Long userNo);

//    @Query()
//    void updatePassword(@Param("email") String email, @Param("tempPassword")String tempPassword);
}
