package com.majid.secureFileShare.service;

import org.springframework.stereotype.Service;
import com.majid.secureFileShare.repository.UserRepository;
import com.majid.secureFileShare.model.User;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser (String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
