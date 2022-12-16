package com.github.ypl.simplyblog.repository;

import com.github.ypl.simplyblog.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    int countByUserId(int userId);

    @Modifying
    void deleteAllByUserId(int userId);
}
