package com.nhngcmnh.example.identity_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@Slf4j
public class SecurityConfig {

    @Bean
    public org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String scope = jwt.getClaimAsString("scope");
            if (scope == null) return java.util.Collections.emptyList();
            java.util.List<org.springframework.security.core.GrantedAuthority> authorities = new java.util.ArrayList<>();
            for (String role : scope.split(" ")) {
                authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("" + role));
            }
            return authorities;
        });
        return converter;
    }
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/users", "/auth/login", "/auth/introspect","/users/*").permitAll()
                .requestMatchers(HttpMethod.PUT, "/auth/login","/users","/users/*").permitAll()
                 .requestMatchers(HttpMethod.GET, "/auth/login","/users").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                jwt.decoder(customJwtDecoder);
                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
            }));
        return http.build();
    }

}
