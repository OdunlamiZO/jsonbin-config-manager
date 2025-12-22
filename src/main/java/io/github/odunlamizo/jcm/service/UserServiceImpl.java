package io.github.odunlamizo.jcm.service;

import io.github.odunlamizo.jcm.model.Role;
import io.github.odunlamizo.jcm.model.User;
import io.github.odunlamizo.jcm.repository.UserRepository;
import io.github.odunlamizo.todus.annotation.Todo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        if (Objects.isNull(newPassword) || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("New password and confirmation do not match");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username).orElseThrow();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setLastSeen(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAllByDeletedAtIsNull();
        // Ensure passwords are not exposed when returning users
        users.forEach(user -> user.setPassword(null));

        return users;
    }

    @Override
    @Todo(value = "Decide what to do with deleting ADMIN", assignee = "odunlamizo")
    public void deleteUser(int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (Objects.nonNull(user)) {
            if (user.getRole() == Role.ADMIN) {
                throw new IllegalStateException("Admin users cannot be deleted");
            }
            user.setDeletedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    @Override
    public void addUser(String name, String email, String password, Role role) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (Objects.isNull(email) || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (Objects.isNull(password) || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        // Prevent duplicate active user with the same email
        User existingActive = userRepository.findByEmailAndDeletedAtIsNull(email).orElse(null);
        if (Objects.nonNull(existingActive)) {
            throw new IllegalArgumentException("A user with this email already exists");
        }

        // If there is a soft-deleted user with the same email, revive it
        User existingInactive = userRepository.findByEmail(email).orElse(null);
        if (Objects.nonNull(existingInactive)) {
            existingInactive.setName(name);
            existingInactive.setPassword(passwordEncoder.encode(password));
            existingInactive.setRole(Objects.nonNull(role) ? role : Role.VIEWER);
            existingInactive.setDeletedAt(null);
            existingInactive.setLastSeen(null);
            userRepository.save(existingInactive);
            return;
        }

        User user =
                User.builder()
                        .name(name)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .role(Objects.nonNull(role) ? role : Role.VIEWER)
                        .lastSeen(null)
                        .build();
        userRepository.save(user);
    }
}
