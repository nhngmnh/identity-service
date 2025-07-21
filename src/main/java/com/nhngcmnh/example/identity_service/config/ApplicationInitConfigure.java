package com.nhngcmnh.example.identity_service.config;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nhngcmnh.example.identity_service.entity.User;
import com.nhngcmnh.example.identity_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfigure {
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return _ -> {
            log.info("[INIT] Kiểm tra tài khoản admin...");
            if (userRepository.findByUsername("admin").isEmpty()) {
                log.info("[INIT] Chưa có admin, tiến hành tạo mới.");
                var role = new HashSet<String>();
                role.add("ADMIN");
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(role)
                        .build();
                userRepository.save(admin);
                log.info("[INIT] Đã tạo admin mặc định với username=admin, password=admin, roles={}", role);
            } else {
                log.info("[INIT] Đã tồn tại tài khoản admin.");
            }
        };
    }

}
