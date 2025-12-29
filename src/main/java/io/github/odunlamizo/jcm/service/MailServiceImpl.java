package io.github.odunlamizo.jcm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.host:}")
    private String mailHost;

    @Override
    public boolean isSmtpEnabled() {
        return !mailHost.isBlank();
    }

    @Override
    public void sendUserCredentials(String email, String plainPassword) {
        String message =
                """
        =====================================
        To: %s
        Subject: Your Account Credentials

        Hello,

        Your account has been created. Here are your credentials:

        Email: %s
        Password: %s

        Please change your password after logging in for the first time.

        Best regards,
        Your Team
        =====================================
        """
                        .formatted(email, email, plainPassword);

        System.out.println(message);
    }
}
