package com.proj.telegramkafka.repository;

import com.proj.telegramkafka.model.TelegramUser;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class TelegramUserRepositoryTests {

    private final TelegramUserRepository telegramUserRepository;

    @Autowired
    public TelegramUserRepositoryTests(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    @Test
    public void testInjectedComponentForNull() {
        AssertionsForClassTypes.assertThat(telegramUserRepository).isNotNull();
    }

    @Test
    public void testValidFindByUsername() {
        String username = "username";
        TelegramUser expected = telegramUserRepository.save(new TelegramUser(username, 123));
        Optional<TelegramUser> actual = telegramUserRepository.findByUsername(username);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void testInvalidFindByUsername() {
        Optional<TelegramUser> actual = telegramUserRepository.findByUsername("invalid");
        assertTrue(actual.isEmpty());
    }
}
