package com.proj.telegramkafka.controller;

import com.proj.telegramkafka.model.TelegramUser;
import com.proj.telegramkafka.service.TelegramUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/telegram-user")
public class TelegramUserController {

    private final TelegramUserService telegramUserService;

    @PostMapping("/create")
    public void createTelegramUser(String username, String chatId) {
        telegramUserService.create(username, chatId);
        log.info("POST-TELEGRAM_USER === {} == {}", username, LocalDateTime.now());
    }

    @GetMapping("/{username}")
    public TelegramUser getUserByUsername(@PathVariable String username) {
        var telegramUser = telegramUserService.readByUsername(username);
        log.info("GET-TELEGRAM_USER === {}", username);
        return telegramUser;
    }
}
