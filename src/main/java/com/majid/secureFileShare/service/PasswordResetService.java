package com.majid.secureFileShare.service;

import com.majid.secureFileShare.Exception.InvalidTokenException;
import com.majid.secureFileShare.model.PasswordResetToken;
import com.majid.secureFileShare.model.User;
import com.majid.secureFileShare.repository.PasswordResetRepository;
import com.majid.secureFileShare.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;


@Service
public class PasswordResetService {
    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public String generateToken() {
    byte [] randomBytes = new byte[24];
    new SecureRandom().nextBytes(randomBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public Optional<PasswordResetToken> findByUser(User user) {
        return passwordResetRepository.findByUser(user);
    }

    public void deleteByToken(String resetToken) {
        passwordResetRepository.deleteByToken(resetToken);
    }
    public void sendRestEmailToUser(User user, String resetlink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Do not reply, password reset");
        message.setText("We received a request to reset password for this email: " + user.getEmail() + "\n"+
        "Please use the following link to reset the password" + resetlink);
        mailSender.send(message);
    }

    @Transactional
    public void createPasswordResetToken(User user, PasswordResetToken resetToken) {

        String token = generateToken();
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        resetToken.setUser(user);
        passwordResetRepository.save(resetToken);
        String resetLink = "http://localhost:8080/auth/reset-password?token=" + token;
        sendRestEmailToUser(user, resetLink);
    }




    public void resetPassword (String token, String newPassword) {

        PasswordResetToken resetToken =checkTokenValidity(token, "Password reset failed: token invalid",
                "Password reset failed: Reset token has expired");


        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetRepository.delete(resetToken);

    }


    //this method checks if the token existence and expiration
    public PasswordResetToken checkTokenValidity(String token, String invalidToken, String expiredToken) {
       PasswordResetToken resetToken = passwordResetRepository
                .findByToken(token)
                .orElseThrow(() -> new InvalidTokenException(invalidToken));

       if(resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
           passwordResetRepository.delete(resetToken);
           throw new InvalidTokenException(expiredToken);
       }
        return resetToken;
    }

}
