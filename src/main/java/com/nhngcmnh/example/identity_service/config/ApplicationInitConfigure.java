package com.nhngcmnh.example.identity_service.config;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;
import com.nhngcmnh.example.identity_service.repository.UserRepository;
import com.nhngcmnh.example.identity_service.service.UserService;
import com.nhngcmnh.example.identity_service.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfigure {
    @Bean
    ApplicationRunner applicationRunner(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        return _ -> {
            log.info("[INIT] Kiểm tra tài khoản admin...");
            if (userRepository.findByUsername("admin").isEmpty()) {
                com.nhngcmnh.example.identity_service.dto.request.UserCreationRequest adminRequest = new com.nhngcmnh.example.identity_service.dto.request.UserCreationRequest();
                adminRequest.setUsername("admin");
                adminRequest.setPassword("admin");
                adminRequest.setFirstName("Admin");
                adminRequest.setLastName("System");
                adminRequest.setDob(java.time.LocalDate.of(2000,1,1));
                userService.createUser(adminRequest);
                log.info("[INIT] Đã tạo admin mặc định với username=admin, password=admin, role=ADMIN");
            } else {
                log.info("[INIT] Đã tồn tại tài khoản admin.");
            }
        };
    }

}
