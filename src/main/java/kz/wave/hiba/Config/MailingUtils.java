package kz.wave.hiba.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailingUtils {

    @Value("${mail.subject}")
    private String subject;

    @Value("${mail.body.template}")
    private String bodyTemplate;

    @Autowired
    private JavaMailSender mailSender;

    public void sendPass(String email, String password) {
        String body = String.format(bodyTemplate, password);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

}
