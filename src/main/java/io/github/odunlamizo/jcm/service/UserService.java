package io.github.odunlamizo.jcm.service;

import io.github.odunlamizo.jcm.model.Role;
import io.github.odunlamizo.jcm.model.User;
import org.springframework.data.domain.Page;

public interface UserService {

    void changePassword(String currentPassword, String newPassword, String confirmPassword);

    Page<User> getUsers(int page, int size);

    Page<User> getUsers(int page, int size, String search, Role role);

    void deleteUser(int userId);

    void addUser(String name, String email, String password, Role role);

    void editUser(int userId, String name, Role role);
}
