package com.edts.concert.repository;

import com.edts.concert.entity.RefreshToken;
import com.edts.concert.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteAllByUser(User user);
    Optional<RefreshToken> findByToken(String token);
}
