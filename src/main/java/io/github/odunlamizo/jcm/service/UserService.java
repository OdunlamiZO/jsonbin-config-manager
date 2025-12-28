package io.github.odunlamizo.jcm.service;

import io.github.odunlamizo.jcm.model.Role;
import io.github.odunlamizo.jcm.model.User;
import java.util.List;

public interface UserService {

    void changePassword(String currentPassword, String newPassword, String confirmPassword);

    List<User> getAllUsers();

    void deleteUser(int userId);

    void addUser(String name, String email, String password, Role role);

    void editUser(int userId, String name, Role role);
}
