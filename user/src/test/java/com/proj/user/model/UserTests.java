package com.proj.user.model;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.stream.Stream;

import static com.proj.user.TestAdvice.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTests {

    private User user;

    @BeforeEach
    public void setCorrectUser() {
        user = new User();
        user.setId(1);
        user.setFullName("Correct User");
        user.setEmail("correct@mail.com");
        user.setNotificationType(NotificationType.EMAIL);
        user.setTelegram("@correct");
        user.setAge(23);
        user.setPassword("1234");
        user.setProvider(Provider.GOOGLE);
        user.setRole(Role.of("ADMIN"));
    }

    @Test
    public void testCorrectUser() {
        assertEquals(0, getViolation(user).size());
    }

    @ParameterizedTest
    @MethodSource("getEmptyAndNullArguments")
    public void testInvalidUsersFullNameField(String fullName, String error) {
        user.setFullName(fullName);
        Set<ConstraintViolation<User>> violationSet = getViolation(user);

        assertEquals(1, violationSet.size());
        assertEquals(error, violationSet.iterator().next().getInvalidValue());
        assertEquals("Fill in the field with the name.", violationSet.iterator().next().getMessage());
    }

    @ParameterizedTest
    @MethodSource("getEmptyAndNullArguments")
    public void testInvalidUsersPasswordField(String password, String error) {
        user.setPassword(password);
        Set<ConstraintViolation<User>> violationSet = getViolation(user);

        assertEquals(1, violationSet.size());
        assertEquals(error, violationSet.iterator().next().getInvalidValue());
        assertEquals("The password cannot be 'blank'", violationSet.iterator().next().getMessage());
    }

    private static Stream<Arguments> getEmptyAndNullArguments() {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("getEmailArguments")
    public void testInvalidUsersEmailField(String email, String error) {
        user.setEmail(email);
        Set<ConstraintViolation<User>> violationSet = getViolation(user);

        assertEquals(1, violationSet.size());
        assertEquals(error, violationSet.iterator().next().getInvalidValue());
        assertEquals("Must be a valid e-mail address", violationSet.iterator().next().getMessage());
    }

    private static Stream<Arguments> getEmailArguments() {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of("@.", "@."),
                Arguments.of("invalid@", "invalid@"),
                Arguments.of("error@gmail", "error@gmail"),
                Arguments.of("error@gmail.", "error@gmail."),
                Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("getTelegramArguments")
    public void testInvalidUsersTelegramField(String telegram, String error) {
        user.setTelegram(telegram);
        Set<ConstraintViolation<User>> violationSet = getViolation(user);

        assertEquals(1, violationSet.size());
        assertEquals(error, violationSet.iterator().next().getInvalidValue());
        assertEquals("The telegram account must started with '@' character", violationSet.iterator().next().getMessage());
    }

    private static Stream<Arguments> getTelegramArguments() {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of("@", "@"),
                Arguments.of(null, null)
        );
    }

    @Test
    public void testInvalidUsersNotificationTypeField() {
        user.setNotificationType(null);
        Set<ConstraintViolation<User>> violationSet = getViolation(user);

        assertEquals(1, violationSet.size());
        assertEquals(null, violationSet.iterator().next().getInvalidValue());
        assertEquals("Select where we can send you notifications", violationSet.iterator().next().getMessage());
    }
}
