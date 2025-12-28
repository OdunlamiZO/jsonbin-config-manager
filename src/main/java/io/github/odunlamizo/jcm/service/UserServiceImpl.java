package io.github.odunlamizo.jcm.service;

import io.github.odunlamizo.jcm.model.Role;
import io.github.odunlamizo.jcm.model.User;
import io.github.odunlamizo.jcm.repository.UserRepository;
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
    public void deleteUser(int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (Objects.nonNull(user)) {
            if (user.getRole() == Role.SUPER_ADMIN) {
                throw new IllegalStateException("Super admin cannot be deleted");
            }

            if (user.getRole() == Role.ADMIN) {
                String actorEmail =
                        SecurityContextHolder.getContext().getAuthentication().getName();
                User actor = userRepository.findByEmail(actorEmail).orElseThrow();
                if (actor.getRole() != Role.SUPER_ADMIN) {
                    throw new IllegalStateException("Only the super admin can delete admin users");
                }
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

        Role roleToAssign = Objects.nonNull(role) ? role : Role.VIEWER;

        // Super admin is reserved for the initially bootstrapped user only
        if (roleToAssign == Role.SUPER_ADMIN) {
            throw new IllegalArgumentException("Super admin cannot be assigned");
        }

        User existingActive = userRepository.findByEmailAndDeletedAtIsNull(email).orElse(null);
        if (Objects.nonNull(existingActive)) {
            throw new IllegalArgumentException("A user with this email already exists");
        }

        // If there is a soft-deleted user with the same email, revive it
        User existingInactive = userRepository.findByEmail(email).orElse(null);
        if (Objects.nonNull(existingInactive)) {
            existingInactive.setName(name);
            existingInactive.setPassword(passwordEncoder.encode(password));
            existingInactive.setRole(roleToAssign);
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
                        .role(roleToAssign)
                        .lastSeen(null)
                        .build();
        userRepository.save(user);
    }

    @Override
    public void editUser(int userId, String name, Role role) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getRole() == Role.SUPER_ADMIN) {
            throw new IllegalStateException("Super admin cannot be edited");
        }

        if (user.getRole() == Role.ADMIN) {
            String actorEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            User actor = userRepository.findByEmail(actorEmail).orElseThrow();
            if (actor.getRole() != Role.SUPER_ADMIN) {
                throw new IllegalStateException("Only the super admin can edit admin users");
            }
        }

        Role roleToAssign = Objects.nonNull(role) ? role : user.getRole();

        // Super admin is reserved for the initially bootstrapped user only
        if (roleToAssign == Role.SUPER_ADMIN) {
            throw new IllegalArgumentException("Super admin cannot be assigned");
        }

        user.setName(name);
        user.setRole(roleToAssign);
        userRepository.save(user);
    }
}
