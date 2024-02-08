package com.proj.user.service;

import com.proj.user.dto.NotificationData;
import com.proj.user.model.NotificationType;
import com.proj.user.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@AllArgsConstructor
public class NotificationService {

    private final UserService userService;
    private final RestTemplate restTemplate;

    public void sendDataForNotification(String recipientEmail, long senderId) {
        User recipient = userService.readByEmail(recipientEmail);
        User sender = userService.readById(senderId);
        log.info("{} want to receive a message from {}", recipientEmail, sender.getEmail());
        chooseUrlForSending(recipient, sender);
    }

    private void chooseUrlForSending(User recipient, User sender) {
        NotificationData notificationData = NotificationData.of(recipient, sender);

        if (isUsersNotificationTypeTelegram(recipient.getId())) {
            sendDataToUrl("http://TELEGRAM/api/v1/telegram/send", notificationData);
        } else {
            sendDataToUrl("http://EMAIL/api/v1/email/send", notificationData);
        }
    }

    public boolean isUsersNotificationTypeTelegram(long id) {
        return userService.readById(id).getNotificationType().equals(NotificationType.TELEGRAM);
    }

    private void sendDataToUrl(String url, NotificationData notificationData) {
        restTemplate.postForLocation(url, notificationData);
    }
}
