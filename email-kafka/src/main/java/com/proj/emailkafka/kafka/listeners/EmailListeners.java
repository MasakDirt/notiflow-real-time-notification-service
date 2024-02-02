package com.proj.emailkafka.kafka.listeners;

import com.proj.emailkafka.EmailSender;
import com.proj.emailkafka.model.NotificationData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class EmailListeners {

    private final EmailSender emailSender;

    @KafkaListener(
            topics = "email",
            groupId = "email_id"
    )
    void listener(String jsonData) {
        NotificationData notificationData = NotificationData.fromJsonString(jsonData);
        log.info("Listener received: {}", notificationData);
        emailSender.sendEmailNotification(notificationData);
    }
}
