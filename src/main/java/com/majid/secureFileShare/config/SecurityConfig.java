package com.majid.secureFileShare.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//BCryptPasswordEncoder is the class used to hash the password
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // ✅ correct modern syntax
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/api/health").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {})
                .formLogin(form -> form.disable()); // disables the default login page

        return http.build();
    }
}
