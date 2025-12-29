package io.github.odunlamizo.jcm.service;

import io.github.odunlamizo.jcm.model.Role;
import io.github.odunlamizo.jcm.model.User;
import io.github.odunlamizo.jcm.repository.UserRepository;
import io.github.odunlamizo.jcm.util.PasswordGenerator;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final MailService mailService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final PasswordGenerator passwordGenerator;

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
    public Page<User> getUsers(int page, int size) {
        return getUsers(page, size, null, null);
    }

    @Override
    public Page<User> getUsers(int page, int size, String search, Role role) {
        Page<User> userPage;
        if ((Objects.isNull(search) || search.isBlank()) && Objects.isNull(role)) {
            userPage = userRepository.findAllByDeletedAtIsNull(PageRequest.of(page, size));
        } else {
            String term = (Objects.nonNull(search) && !search.isBlank()) ? search.trim() : "";
            userPage = userRepository.searchActive(term, role, PageRequest.of(page, size));
        }
        userPage.forEach(user -> user.setPassword(null));
        return userPage;
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

        boolean smtpEnabled = mailService.isSmtpEnabled();
        String finalPassword = smtpEnabled ? passwordGenerator.generate() : password;

        if (Objects.isNull(finalPassword) || finalPassword.isBlank()) {
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
            existingInactive.setPassword(passwordEncoder.encode(finalPassword));
            existingInactive.setRole(roleToAssign);
            existingInactive.setDeletedAt(null);
            existingInactive.setLastSeen(null);
            userRepository.save(existingInactive);
        } else {
            User user =
                    User.builder()
                            .name(name)
                            .email(email)
                            .password(passwordEncoder.encode(finalPassword))
                            .role(roleToAssign)
                            .build();
            userRepository.save(user);
        }

        if (smtpEnabled) {
            mailService.sendUserCredentials(email, finalPassword);
        }
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
