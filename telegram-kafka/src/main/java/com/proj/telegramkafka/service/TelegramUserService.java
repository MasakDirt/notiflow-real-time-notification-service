package com.proj.telegramkafka.service;

import com.proj.telegramkafka.model.TelegramUser;
import com.proj.telegramkafka.repository.TelegramUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@AllArgsConstructor
public class TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;

    public TelegramUser create(String username, String chatId) {
        TelegramUser telegramUser = new TelegramUser(username, chatId);
        telegramUserRepository.saveAndFlush(telegramUser);
        log.info("Saved user - {}, with {} - chat id.", username, chatId);
        return telegramUser;
    }

    public TelegramUser readByUsername(String username) {
        return telegramUserRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Telegram user not found"));
    }
}
