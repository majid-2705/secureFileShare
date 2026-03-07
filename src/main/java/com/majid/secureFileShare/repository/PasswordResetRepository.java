package com.majid.secureFileShare.repository;

import com.majid.secureFileShare.model.PasswordResetToken;
import com.majid.secureFileShare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token) ;
    Optional<PasswordResetToken> findByUser(User user);
    void deleteByToken(String token);
}
