package io.github.odunlamizo.jcm.config;

import io.github.odunlamizo.jcm.model.User;
import io.github.odunlamizo.jcm.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(
                        formLogin ->
                                formLogin
                                        .loginPage("/login")
                                        .usernameParameter("email")
                                        .successHandler(authenticationSuccessHandler())
                                        .failureUrl("/login?error=true"))
                .logout(logout -> logout.logoutSuccessUrl("/login"))
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers(
                                                "/login",
                                                "/css/**",
                                                "/js/**",
                                                "/images/**",
                                                "/favicon.ico",
                                                "/h2-console/**")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.POST, "/user/**")
                                        .hasAnyRole("SUPER_ADMIN", "ADMIN")
                                        .anyRequest()
                                        .authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(
                        headers ->
                                headers.frameOptions(
                                        HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String username = authentication.getName();

            User user = userRepository.findByEmailAndDeletedAtIsNull(username).orElse(null);
            if (user != null) {
                user.setLastSeen(LocalDateTime.now());
                userRepository.save(user);
            }

            response.sendRedirect("/dashboard");
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user =
                    userRepository
                            .findByEmailAndDeletedAtIsNull(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
