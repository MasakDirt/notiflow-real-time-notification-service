package com.proj.user.service;

import com.proj.user.dto.AddDataRequest;
import com.proj.user.model.*;
import com.proj.user.repository.UserRepository;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceTests(UserService userService, UserRepository userRepository,
                            RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    public void testInjectedComponents() {
        assertThat(userService).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(roleService).isNotNull();
        assertThat(passwordEncoder).isNotNull();
    }

    @Test
    public void testValidEncodingPasswordInSaveUserWithSettingFieldsMethod() {
        String password = "encode me";
        User expected = newUserWithSomeFields();
        expected.setPassword(password);
        User actual = userService.
                saveWithSettingFields(expected, expected.getRole().getName());

        assertTrue(passwordEncoder.matches(password, actual.getPassword()));
    }

    @Test
    public void testValidSetRoleInSaveUserWithSettingFieldsMethod() {
        User expected = newUserWithSomeFields();
        String roleName = expected.getRole().getName();
        User actual = userService.
                saveWithSettingFields(expected, roleName);

        assertEquals(roleName, actual.getRole().getName());
    }

    @Test
    public void testValidSaveUserWithSettingFieldsAndLocalProvider() {
        int usersSizeBeforeSave = userRepository.findAll().size();

        User expected = newUserWithSomeFields();
        User actual = userService.
                saveWithSettingFields(expected, expected.getRole().getName());

        int usersSizeAfterSave = userRepository.findAll().size();

        assertTrue(usersSizeBeforeSave < usersSizeAfterSave);
        assertEquals(expected, actual);
        assertEquals(Provider.LOCAL, actual.getProvider());
    }


    @Test
    public void testValidSaveUserWithSettingFieldsAndGoogleProvider() {
        int usersSizeBeforeSave = userRepository.findAll().size();

        User expected = newUserWithSomeFields();
        expected.setProvider(Provider.GOOGLE);
        User actual = userService.
                saveWithSettingFields(expected, expected.getRole().getName());

        int usersSizeAfterSave = userRepository.findAll().size();

        assertTrue(usersSizeBeforeSave < usersSizeAfterSave);
        assertEquals(expected, actual);
        assertEquals(Provider.GOOGLE, actual.getProvider());
    }

    private User newUserWithSomeFields() {
        User user = new User();
        user.setFullName("Creation");
        user.setAge(22);
        user.setTelegram("@telegram");
        user.setEmail("users@mail.co");
        user.setRole(roleService.createWithValidName("TEST"));
        user.setNotificationType(NotificationType.EMAIL);
        user.setPassword("1234");
        return user;
    }

    @ParameterizedTest
    @MethodSource("getInvalidArgumentsForEntityNotFoundException")
    public void testInvalidSaveUserWithSettingFieldsThrowingEntityNotFoundException(String roleName) {
        assertThrows(EntityNotFoundException.class,
                () -> userService.saveWithSettingFields(newUserWithSomeFields(), roleName));
    }

    @ParameterizedTest
    @MethodSource("getInvalidArgumentsForEntityNotFoundException")
    public void testInvalidReadByEmailThrowingEntityNotFoundException(String email) {
        assertThrows(EntityNotFoundException.class,
                () -> userService.readByEmail(email));
    }

    private static Stream<String> getInvalidArgumentsForEntityNotFoundException() {
        return Stream.of("Invalid", " ", null);
    }

    @Test
    public void testValidReadByEmail() {
        User expected = userService.
                saveWithSettingFields(newUserWithSomeFields(), "TEST");
        User actual = userService.readByEmail(expected.getEmail());

        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidSaveUserWithSettingFieldsThrowingIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.saveWithSettingFields(new User(), "TEST"));
    }

    @Test
    public void testInvalidSaveUserWithSettingFieldsThrowingNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> userService.saveWithSettingFields(null, "TEST"));
    }

    @Test
    public void testUserExist() {
        User created = userService.
                saveWithSettingFields(newUserWithSomeFields(), "TEST");
        String email = created.getEmail();

        assertTrue(userService.isUserExist(email));
    }

    @Test
    public void testUserNotExist() {
        String email = "Not Exist";
        assertFalse(userService.isUserExist(email));
    }

    @Test
    public void testValidCreateNewUserFromOAuth2() {
        Role expectedRole = roleService.readByName("USER");
        CustomOAuth2User customOAuth2User = createOAuth2User();
        User expected = createExpectedUser(customOAuth2User);
        User actual = userService.createNewUserFromOAuth2(customOAuth2User);

        assertEquals(expected.getFullName(), actual.getFullName());
        assertEquals(expected.getTelegram(), actual.getTelegram());
        assertEquals(expectedRole, actual.getRole());
        assertEquals(Provider.GOOGLE, actual.getProvider());
        assertTrue(passwordEncoder.matches(expected.getPassword(), actual.getPassword()));
    }

    private CustomOAuth2User createOAuth2User() {
        Map<String, Object> attributes = Map.of("email", "test@example.com",
                "name", "Maks Korniev",
                "sub", "fdhsuhq38584938458sghuh329485");
        return new CustomOAuth2User(new OAuth2UserStub(attributes));
    }

    private User createExpectedUser(CustomOAuth2User customOAuth2User) {
        User expectedUser = new User();
        expectedUser.setEmail(customOAuth2User.getName());
        expectedUser.setFullName(customOAuth2User.getFullName());
        expectedUser.setPassword(customOAuth2User.getAttributes().get("sub").toString());
        expectedUser.setProvider(Provider.GOOGLE);
        expectedUser.setNotificationType(NotificationType.EMAIL);
        expectedUser.setTelegram("@yourtelegram");
        return expectedUser;
    }

    private static class OAuth2UserStub implements OAuth2User {
        private final Map<String, Object> attributes;

        public OAuth2UserStub(Map<String, Object> attributes) {
            this.attributes = attributes;
        }

        @Override
        public Map<String, Object> getAttributes() {
            return attributes;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.emptyList();
        }

        @Override
        public String getName() {
            return attributes.get("email").toString();
        }
    }

    @Test
    public void testInvalidCreateNewUserFromOAuth2ThrowingNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> userService.createNewUserFromOAuth2(null));
    }

    @Test
    public void testValidReadById() {
        User expected = userService.
                saveWithSettingFields(newUserWithSomeFields(), "TEST");
        User actual = userService.readById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidReadByIdThrowingEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class,
                () -> userService.readById(0));
    }

    @Test
    public void testValidUpdate() {
        String updatedFullName = "Updated FullName";
        int updatedAge = 101;
        NotificationType updatedNotificationType = NotificationType.TELEGRAM;

        User unexpected = userService.
                saveWithSettingFields(newUserWithSomeFields(), "TEST");

        String oldFullName = unexpected.getFullName();
        int oldAge = unexpected.getAge();
        NotificationType oldNotificationType = unexpected.getNotificationType();

        unexpected.setFullName(updatedFullName);
        unexpected.setAge(updatedAge);
        unexpected.setNotificationType(updatedNotificationType);

        User actual = userService.update(unexpected);

        assertNotEquals(oldFullName, actual.getFullName());
        assertNotEquals(oldAge, actual.getAge());
        assertNotEquals(oldNotificationType, actual.getNotificationType());

        assertEquals(updatedFullName, actual.getFullName());
        assertEquals(updatedAge, actual.getAge());
        assertEquals(updatedNotificationType, actual.getNotificationType());
        assertEquals(unexpected.getEmail(), actual.getEmail());
        assertEquals(unexpected.getPassword(), actual.getPassword());
        assertEquals(unexpected.getProvider(), actual.getProvider());
        assertEquals(unexpected.getRole(), actual.getRole());
    }

    @Test
    public void testInvalidUpdateThrowingEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class,
                () -> userService.update(new User()));
    }

    @Test
    public void testInvalidUpdateThrowingNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> userService.update(null));
    }

    @Test
    public void testGetAll() {
        Pageable pageable = Pageable.ofSize(3);
        Page<User> expectedPage = userRepository.findAll(pageable);
        Page<User> actualPage = userService.getAll(pageable);

        assertEquals(expectedPage, actualPage);
    }

    @Test
    public void testCheckValidPasswords() {
        String validPassword = "password";
        User user = newUserWithSomeFields();
        user.setPassword(validPassword);
        userService.saveWithSettingFields(user, "TEST");

        assertDoesNotThrow(() -> userService.checkInvalidPasswords(validPassword, user.getPassword()));
    }

    @Test
    public void testCheckInvalidPasswords() {
        String invalidPassword = "invalid";
        User user = userService.saveWithSettingFields(newUserWithSomeFields(), "TEST");

        assertThrows(ResponseStatusException.class,
                () -> userService.checkInvalidPasswords(invalidPassword, user.getPassword()));
    }

    @Test
    public void testValidAddDataToOAuth2User() {
        int newAge = 23;
        String newNotificationType = "Telegram";
        String newTelegram = "@newtelegram";

        User oldUser = userService.
                saveWithSettingFields(newUserWithSomeFields(), "TEST");
        int oldAge = oldUser.getAge();
        String oldTelegram = oldUser.getTelegram();
        String oldEmail = oldUser.getEmail();
        String oldPassword = oldUser.getPassword();
        NotificationType oldNotificationType = oldUser.getNotificationType();

        long idToUpdate = oldUser.getId();
        AddDataRequest addDataRequest = buildDataRequest(newAge, newNotificationType, newTelegram);

        User updated = userService.addDataToOAuth2User(idToUpdate, addDataRequest);

        assertEquals(idToUpdate, updated.getId());
        assertNotEquals(oldAge, updated.getAge());
        assertNotEquals(oldNotificationType, updated.getNotificationType());
        assertNotEquals(oldTelegram, updated.getTelegram());

        assertEquals(oldEmail, updated.getEmail());
        assertEquals(oldPassword, updated.getPassword());
        assertEquals(newAge, updated.getAge());
        assertEquals(newNotificationType, updated.getNotificationType().getName());
        assertEquals(newTelegram, updated.getTelegram());
    }

    @Test
    public void testInvalidAddDataToOAuth2UserThrowingEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class,
                () -> userService.addDataToOAuth2User(0,
                        buildDataRequest(20, NotificationType.EMAIL.getName(), "@telegram")
                )
        );
    }

    private AddDataRequest buildDataRequest(int age, String notificationType, String telegram) {
        return AddDataRequest.builder().
                age(age).
                notificationType(notificationType).
                telegram(telegram).
                build();
    }

    @Test
    public void testInvalidAddDataToOAuth2UserThrowingNullPointerException() {
        User created = userService.
                saveWithSettingFields(newUserWithSomeFields(), "TEST");
        assertThrows(NullPointerException.class,
                () -> userService.addDataToOAuth2User(created.getId(), null));
    }

    @Test
    public void testValidDelete() {
        User userToDelete = userService.
            saveWithSettingFields(newUserWithSomeFields(), "TEST");

        int usersSizeBeforeDeleting = userRepository.findAll().size();
        userService.delete(userToDelete);
        int usersSizeAfterDeleting = userRepository.findAll().size();

        assertThrows(EntityNotFoundException.class, () -> userService.readById(userToDelete.getId()));
        assertTrue(usersSizeBeforeDeleting > usersSizeAfterDeleting);
    }

    @Test
    public void testInvalidDeleteWithoutThrowing() {
        assertDoesNotThrow(() -> userService.delete(new User()));
    }
}
