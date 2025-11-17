package io.github.odunlamizo.config;

import io.github.odunlamizo.model.Role;
import io.github.odunlamizo.model.User;
import io.github.odunlamizo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserSeeder {

    @Value("${application.admin.email}")
    private String adminEmail;

    @Value("${application.admin.password}")
    private String adminPassword;

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedDefaultUser(UserRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(
                        User.builder()
                                .email(adminEmail)
                                .password(passwordEncoder.encode(adminPassword))
                                .role(Role.ADMIN)
                                .build());

                log.info("🌱 Default admin user created: {}", adminEmail);
            }
        };
    }
}
