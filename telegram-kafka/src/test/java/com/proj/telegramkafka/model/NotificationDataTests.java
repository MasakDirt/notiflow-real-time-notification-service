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
public class NotificationDataTests {
    private NotificationData notificationData;

    @BeforeEach
    public void setNotificationData() {
        notificationData = new NotificationData(
                "@someone", "message");
    }

    @Test
    public void testValidNotificationData() {
        assertEquals(0, getViolation(notificationData).size());
    }

    @ParameterizedTest
    @MethodSource("getInvalidUsersTelegram")
    public void testInvalidRecipientUserTelegram(String recipientUserTelegram, String error) {
        notificationData.setUsername(recipientUserTelegram);
        Set<ConstraintViolation<NotificationData>> violationSet = getViolation(notificationData);

        assertEquals(1, violationSet.size());
        assertEquals(error, violationSet.iterator().next().getInvalidValue());
        assertEquals("The telegram account must started with '@' character", violationSet.iterator().next().getMessage());

    }

    private static Stream<Arguments> getInvalidUsersTelegram() {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of("@", "@"),
                Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("getEmptyAndNullArguments")
    public void testInvalidMessage(String message, String error) {
        notificationData.setMessage(message);
        Set<ConstraintViolation<NotificationData>> violationSet = getViolation(notificationData);

        assertEquals(1, violationSet.size());
        assertEquals(error, violationSet.iterator().next().getInvalidValue());
        assertEquals("Message must not be empty!", violationSet.iterator().next().getMessage());

    }

    private static Stream<Arguments> getEmptyAndNullArguments() {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of(null, null)
        );
    }

    @Test
    public void testValidFromJsonString() {
        String recipientUserTelegram = "@recepient";
        String message = "message...";
        NotificationData expected = new NotificationData(recipientUserTelegram, message);
        String jsonString = String.format("{\"username\" : \"%s\", \"message\" : \"%s\"}",
                recipientUserTelegram, message);
        NotificationData actual = NotificationData.fromJsonString(jsonString);
        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidFromJsonString() {
        assertThrows(RuntimeException.class,() -> NotificationData
                .fromJsonString("invalid json"));
    }
}
