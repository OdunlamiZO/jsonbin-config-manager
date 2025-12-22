package io.github.odunlamizo.jcm.config;

import io.github.odunlamizo.jcm.model.Role;
import io.github.odunlamizo.jcm.model.User;
import io.github.odunlamizo.jcm.repository.UserRepository;
import io.github.odunlamizo.jsonbin.JsonBin;
import io.github.odunlamizo.jsonbin.okhttp.JsonBinOkHttp;
import java.time.LocalDateTime;
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
public class Config {

    @Value("${application.admin.name}")
    private String adminName;

    @Value("${application.admin.email}")
    private String adminEmail;

    @Value("${application.admin.password}")
    private String adminPassword;

    @Value("${io.github.odunlamizo.jsonbin.master-key}")
    private String jsonBinMasterKey;

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedDefaultUser(UserRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(
                        User.builder()
                                .name(adminName)
                                .email(adminEmail)
                                .password(passwordEncoder.encode(adminPassword))
                                .lastSeen(LocalDateTime.now())
                                .role(Role.ADMIN)
                                .build());

                log.info("🌱 Default admin user created: {}", adminEmail);
            }
        };
    }

    @Bean
    JsonBin jsonBin() {
        return new JsonBinOkHttp.Builder().withMasterKey(jsonBinMasterKey).build();
    }
}
