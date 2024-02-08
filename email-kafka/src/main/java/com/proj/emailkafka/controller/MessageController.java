package com.proj.emailkafka.controller;

import com.proj.emailkafka.model.NotificationData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/email")
public class MessageController {

    private final KafkaTemplate<String, NotificationData> kafkaTemplate;

    @PostMapping("/send")
    public void sendEmailNotification(@Valid @RequestBody NotificationData notificationData) {
        kafkaTemplate.send("email", notificationData);
    }
}
