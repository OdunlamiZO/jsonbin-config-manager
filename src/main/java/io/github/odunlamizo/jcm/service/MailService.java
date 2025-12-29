package io.github.odunlamizo.jcm.service;

public interface MailService {

    boolean isSmtpEnabled();

    void sendUserCredentials(String email, String plainPassword);
}
