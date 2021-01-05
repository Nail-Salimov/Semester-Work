package server.addition.email.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import server.addition.email.config.MailConfig;
import server.addition.email.contructors.EmailCreator;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class MailConfirmationServiceImpl implements MailConfirmationService {

    @Value("${db.mail}")
    private String username;
    @Value("${db.passwordMail}")
    private String password;
    private Properties props;

    @Autowired
    @Qualifier("confirm_message_creator")
    private EmailCreator emailCreator;

    @Autowired
    private MailConfig config;



    public void send(String subject, String fromEmail, String toEmail, String... data) {
       props = config.getProperties();

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);

            //от кого
            message.setFrom(new InternetAddress(username));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //Заголовок письма
            message.setSubject(subject);
            //Содержимое

            String text = emailCreator.createEmail(data);
            message.setText(text);

            message.setContent(text, "text/html");


            //Отправляем сообщение
            Transport.send(message);
        } catch (MessagingException e) {
            //спросить
            throw new IllegalStateException(e);
        }
    }
}
