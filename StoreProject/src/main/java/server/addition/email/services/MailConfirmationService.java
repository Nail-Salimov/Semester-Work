package server.addition.email.services;

public interface MailConfirmationService {
    void send(String subject, String fromEmail, String toEmail, String... data);
}
