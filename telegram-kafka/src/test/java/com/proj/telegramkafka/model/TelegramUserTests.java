package com.proj.telegramkafka.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Stream;

import static com.proj.telegramkafka.TelegramTestAdvice.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TelegramUserTests {

    private TelegramUser telegramUser;

    @BeforeEach
    public void setTelegramUser() {
        telegramUser = new TelegramUser("username", 100L);
    }

    @Test
    public void testValidTelegramUser() {
        assertEquals(0, getViolation(telegramUser).size());
    }

    @ParameterizedTest
    @MethodSource("getEmptyAndNullArguments")
    public void testInvalidUsername(String username, String error) {
        telegramUser.setUsername(username);
        Set<ConstraintViolation<TelegramUser>> violationSet = getViolation(telegramUser);

        assertEquals(1, violationSet.size());
        assertEquals(error, violationSet.iterator().next().getInvalidValue());
        assertEquals("The username cannot be empty!", violationSet.iterator().next().getMessage());
    }

    private static Stream<Arguments> getEmptyAndNullArguments() {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of(null, null)
        );
    }
}
