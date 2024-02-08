package com.proj.emailkafka.email;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.proj.emailkafka.model.NotificationData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

import static com.google.api.services.gmail.GmailScopes.*;
import static javax.mail.Message.RecipientType.*;

@Slf4j
@Component
@AllArgsConstructor
public class EmailSender {

    private static final String SENDER_EMAIL_ADDRESS = "maksimkarulet8@gmail.com";

    public void sendEmail(NotificationData notificationData) throws Exception {
        Gmail service = getGmailService();
        MimeMessage email = getMimeMessageEmail(notificationData);
        Message message = getNewEncodedMessage(email);
        tryToSendMessageToUser(message, service);
    }

    private Gmail getGmailService() throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        return new Gmail.Builder(httpTransport, jsonFactory, this.getCredentials(httpTransport, jsonFactory))
                .setApplicationName("Sender Test")
                .build();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, GsonFactory jsonFactory)
            throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory,
                new InputStreamReader(Objects.requireNonNull(EmailSender.class.getClassLoader().getResourceAsStream(
                        "client_secret.json"))));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, jsonFactory, clientSecrets, Set.of(GMAIL_SEND)
        )
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8040).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private MimeMessage getMimeMessageEmail(NotificationData notificationData) throws Exception {
        Session session = getSession();
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(SENDER_EMAIL_ADDRESS));
        email.addRecipients(TO, notificationData.getUsername());
        email.setSubject("You subscribed to Notiflow Notifications! Thanks❤️🤖");
        email.setText(notificationData.getMessage());

        return email;
    }

    private Session getSession() {
        return Session.getDefaultInstance(new Properties(), null);
    }

    private Message getNewEncodedMessage(MimeMessage email) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);
        return new Message().setRaw(encodedEmail);
    }

    private void tryToSendMessageToUser(Message message, Gmail service) throws Exception {
        try {
            message = service.users().messages().send("me", message).execute();
            log.info("Message id {}", message.getId());
            log.info("{}", message.toPrettyString());
        } catch (GoogleJsonResponseException googleJsonResponseException) {
            log.error("Error sending message to email!", googleJsonResponseException);
        }
    }
}
