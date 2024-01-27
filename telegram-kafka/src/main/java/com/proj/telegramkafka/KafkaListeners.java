package com.proj.telegramkafka;

import com.proj.telegramkafka.model.NotificationData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListeners {

    @KafkaListener(
            topics = "telegram",
            groupId = "telegram_bot"
    )
    void listener(String jsonData) {
        NotificationData notificationData = NotificationData.fromJsonString(jsonData);
        log.info("Listener received: {}", notificationData);
    }
}
