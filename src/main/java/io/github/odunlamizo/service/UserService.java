package io.github.odunlamizo.service;

public interface UserService {

    void updatePassword(String currentPassword, String newPassword, String confirmPassword);
}
