package com.nhngcmnh.example.identity_service.config;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nhngcmnh.example.identity_service.entity.User;
import com.nhngcmnh.example.identity_service.repository.UserRepository;
import com.nhngcmnh.example.identity_service.repository.RoleRepository;

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
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return _ -> {
            log.info("[INIT] Kiểm tra tài khoản admin...");
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .build();
                userRepository.save(admin);
                log.info("[INIT] Đã tạo admin mặc định với username=admin, password=admin");
            } else {
                log.info("[INIT] Đã tồn tại tài khoản admin.");
            }
        };
    }

}
