package io.github.odunlamizo.jcm.service;

public interface UserService {

    void changePassword(String currentPassword, String newPassword, String confirmPassword);
}
