package com.bithumb.jwttest.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    @Modifying
    @Query("update User u set u.lastLoginAt = :lastLoginAt where u.email = :email")
    void updatelastLoginAt(@Param("lastLoginAt")LocalDateTime lastLoginAt, String email);
}
