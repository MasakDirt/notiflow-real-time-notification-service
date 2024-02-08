package com.proj.user.dto;

import com.proj.user.model.User;
import com.proj.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Stream;

import static com.proj.user.TestAdvice.getViolation;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class NotificationDataTests {

    private NotificationData notificationData;
    private final UserService userService;

    @Autowired
    public NotificationDataTests(UserService userService) {
        this.userService = userService;
    }

    @BeforeEach
    public void setNotificationData() {
        notificationData = new NotificationData(
                "@username", "message...");
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
        assertEquals("The username must not be empty!", violationSet.iterator().next().getMessage());

    }

    private static Stream<Arguments> getInvalidUsersTelegram() {
        return Stream.of(
                Arguments.of("", ""),
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
    public void testValidOfUsernameEmail() {
        User expectedUser = userService.readByEmail("user@mail.co");
        User forWhomSubscribe = userService.readByEmail("admin@mail.co");
        NotificationData actual = NotificationData.of(expectedUser, forWhomSubscribe);
        assertEquals(expectedUser.getEmail(), actual.getUsername());
    }

    @Test
    public void testValidOfUsernameTelegram() {
        User expectedUser = userService.readByEmail("admin@mail.co");
        User forWhomSubscribe = userService.readByEmail("user@mail.co");
        NotificationData actual = NotificationData.of(expectedUser, forWhomSubscribe);
        assertEquals(expectedUser.getTelegram(), actual.getUsername());
    }

    @Test
    public void testValidOfMessage() {
        User recipient = userService.readByEmail("admin@mail.co");
        User forWhomSubscribe = userService.readByEmail("user@mail.co");
        NotificationData actual = NotificationData.of(recipient, forWhomSubscribe);
        assertEquals(getMessage(recipient, forWhomSubscribe), actual.getMessage());
    }

    private String getMessage(User recipient, User forWhomSubscribe) {
        String forWhomSubscribeUserName = forWhomSubscribe.getFullName();
        return String.format("""
                        Hello "%s"👋,
                                      
                        You have successfully subscribed to receive notifications from "%s".\s
                        You will now receive updates and notifications whenever %s posts or shares new content.
                                      
                        Thank you for using our notification service❤️!
                                      
                        Notiflow🤖""", recipient.getFullName(), forWhomSubscribeUserName, forWhomSubscribeUserName);
    }
}
