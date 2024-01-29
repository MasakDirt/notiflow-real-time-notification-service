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

    public void createIfNotExist(String username, long chatId) {
        if (isNotExist(username)) {
            createOne(username, chatId);
        }
    }

    private boolean isNotExist(String username) {
        return telegramUserRepository.findByUsername(username).isEmpty();
    }

    private void createOne(String username, long chatId) {
        telegramUserRepository.save(new TelegramUser(username, chatId));
        log.info("Saved user - {}, with {} - chat id.", username, chatId);
    }

    public TelegramUser readByUsername(String username) {
        return telegramUserRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Telegram user not found"));
    }

    public long getUsersChatId(String username) {
        return readByUsername(username).getChatId();
    }
}
