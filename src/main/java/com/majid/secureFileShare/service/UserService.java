package com.majid.secureFileShare.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.majid.secureFileShare.repository.UserRepository;
import com.majid.secureFileShare.model.User;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser (String email, String password) {
        if(findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // hash the password
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }


    public boolean authenticate(String loginEmail, String loginPassword) {
        Optional<User> userFetchedData = findByEmail(loginEmail);
        if(userFetchedData.isEmpty()) {return false;}
        User user = userFetchedData.get();
        return passwordEncoder.matches(loginPassword, user.getPassword());
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
