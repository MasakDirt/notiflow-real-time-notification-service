package com.proj.telegramkafka.service;

import com.proj.telegramkafka.model.TelegramUser;
import com.proj.telegramkafka.repository.TelegramUserRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class TelegramUserServiceTests {

    private final TelegramUserService telegramUserService;
    private final TelegramUserRepository telegramUserRepository;

    @Autowired
    public TelegramUserServiceTests(TelegramUserService telegramUserService, TelegramUserRepository telegramUserRepository) {
        this.telegramUserService = telegramUserService;
        this.telegramUserRepository = telegramUserRepository;
    }

    @Test
    public void testInjectedComponentForNull() {
        AssertionsForClassTypes.assertThat(telegramUserService).isNotNull();
        AssertionsForClassTypes.assertThat(telegramUserRepository).isNotNull();
    }

    @Test
    public void testValidCreateUserIfNotExist() {
        String username = "username";
        long chatId = 21;
        TelegramUser expected = new TelegramUser(username, chatId);
        createUser(username, chatId);
        Optional<TelegramUser> actual = telegramUserRepository.findByUsername(username);
        assertTrue(actual.isPresent());

        expected.setId(actual.get().getId());
        assertEquals(expected, actual.get());
    }

    @Test
    public void testExistUserCreateIfNotExist() {
        String username = "someone";
        long chatId = 2;

        createUser(username, chatId);
        int sizeBeforeSecondCreation = telegramUserRepository.findAll().size();
        createUser(username, chatId);
        int sizeAfterSecondCreation = telegramUserRepository.findAll().size();

        assertEquals(sizeBeforeSecondCreation, sizeAfterSecondCreation);
    }

    @Test
    public void testConstraintViolationExceptionCreateIfNotExist() {
        assertThrows(ConstraintViolationException.class, () -> createUser("", 124));
    }

    @Test
    public void testValidReadByUsername() {
        String username = "@to find";
        createUser(username, 40);
        assertDoesNotThrow(() -> telegramUserService.readByUsername(username));
    }

    @Test
    public void testValidGetUsersChatId() {
        String username = "@to find";
        long actualChatId = 100;
        createUser(username, actualChatId);

        assertDoesNotThrow(() -> telegramUserService.getUsersChatId(username));
        long expectedChatId = telegramUserService.getUsersChatId(username);

        assertEquals(actualChatId, expectedChatId);
    }

    private void createUser(String username, long chatId) {
        telegramUserService.createIfNotExist(username, chatId);
    }

    @Test
    public void testEntityNotFoundExceptionReadByUsername() {
        assertThrows(EntityNotFoundException.class, () -> telegramUserService.readByUsername("invalid"));
    }

    @Test
    public void testEntityNotFoundExceptionGetUsersChatId() {
        assertThrows(EntityNotFoundException.class, () -> telegramUserService.getUsersChatId("invalid"));
    }
}
