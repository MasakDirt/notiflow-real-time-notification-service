package com.proj.emailkafka;

import com.proj.emailkafka.model.NotificationData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static javax.mail.Message.*;

@Slf4j
@Component
public class EmailSender {

    @Value("${email.address}")
    private String senderEmail;

    @Value("${email.password}")
    private String senderPassword;

    public void sendEmailNotification(NotificationData notificationData) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.name", "Максим Корнєв");
        properties.put("mail.smtp.username", senderEmail);
        properties.put("mail.smtp.password", senderPassword);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");


        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(senderEmail));
            Address address = new InternetAddress(notificationData.getUsername());
            message.setRecipient(RecipientType.TO, address);
            message.setSubject("Notiflow message inform!");
            message.setText(notificationData.getMessage());

            Transport.send(message);
            log.info("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
