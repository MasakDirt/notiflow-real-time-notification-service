package com.proj.telegramkafka.kafka.listeners;

import com.proj.telegramkafka.model.NotificationData;
import com.proj.telegramkafka.service.TelegramUserService;
import com.proj.telegramkafka.telegrambot.NotiflowBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListeners {
    private final TelegramUserService telegramUserService;
    private final NotiflowBot notiflowBot;

    @Autowired
    public KafkaListeners(String token, TelegramUserService telegramUserService) {
        this.telegramUserService = telegramUserService;
        this.notiflowBot = new NotiflowBot(token, telegramUserService);
    }

    @KafkaListener(
            topics = "telegram",
            groupId = "telegram_bot"
    )
    void listener(String jsonData) {
        NotificationData notificationData = NotificationData.fromJsonString(jsonData);
        log.info("Listener received: {}", notificationData);

        sendNotificationToUser(notificationData.getRecipientUserTelegram(), notificationData.getMessage());
    }

    private void sendNotificationToUser(String telegramUsername, String message) {
        long chatId = telegramUserService.getUsersChatId(telegramUsername);
        notiflowBot.sendMsg(chatId, message);
    }
}
