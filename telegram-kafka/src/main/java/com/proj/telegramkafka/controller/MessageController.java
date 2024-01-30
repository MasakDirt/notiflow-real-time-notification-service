package com.proj.telegramkafka.controller;

import com.proj.telegramkafka.model.NotificationData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/telegram")
public class MessageController {

    private final KafkaTemplate<String, NotificationData> kafkaTemplate;

    @PostMapping("/send")
    public void publish(@Valid @RequestBody NotificationData notificationData) {
        kafkaTemplate.send("telegram", notificationData);
    }
}
