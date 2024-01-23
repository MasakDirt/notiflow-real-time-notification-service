package com.proj.user.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NotificationTypeTests {

    @ParameterizedTest
    @MethodSource("getTelegramArguments")
    public void testValidGetTypeFromNameTelegram(String telegram) {
        assertEquals(NotificationType.TELEGRAM,
                NotificationType.getTypeFromName(telegram));
    }

    private static Stream<String> getTelegramArguments() {
        return Stream.of("Telegram", "TELEGRAM", "TELEgram",
                "TElegram", "telegram", "teLEGRAM", "teleGRAM", "telegRAM");
    }

    @ParameterizedTest
    @MethodSource("getEmailArguments")
    public void testValidGetTypeFromNameEmail(String email) {
        assertEquals(NotificationType.EMAIL,
                NotificationType.getTypeFromName(email));
    }

    private static Stream<String> getEmailArguments() {
        return Stream.of("E-mail", "E-MAIL", "e-mail",
                "e-MAIL", "e-maIL", "e-maiL");
    }

    @ParameterizedTest
    @MethodSource("getInvalidArguments")
    public void testInvalidGetTypeFromName(String invalidName) {
        assertThrows(EnumConstantNotPresentException.class, () -> NotificationType.getTypeFromName(invalidName));
    }

    private static Stream<String> getInvalidArguments() {
        return Stream.of("Not found", "Invalid", "e--mail", "e-mal", "telegeram");
    }
}
