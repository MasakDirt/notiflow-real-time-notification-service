package com.proj.user.service;

import com.proj.user.model.User;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class AuthUserServiceTests {

    private final AuthUserService authUserService;
    private final UserService userService;

    @Autowired
    public AuthUserServiceTests(AuthUserService authUserService, UserService userService) {
        this.authUserService = authUserService;
        this.userService = userService;
    }

    @Test
    public void testInjectedComponent() {
        AssertionsForClassTypes.assertThat(authUserService).isNotNull();
    }

    @Test
    public void testIsAdminTrue() {
        assertTrue(authUserService.isAdmin("admin@mail.co"));
    }

    @Test
    public void testIsAdminFalse() {
        assertFalse(authUserService.isAdmin("user@mail.co"));
    }

    @Test
    public void testIsUserAuthWithGoogleTrue() {
        // user has provider GOOGLE
        User user = userService.readByEmail("user@mail.co");
        assertTrue(authUserService.isUserAuthWithGoogle(user.getId()));
    }

    @Test
    public void testIsUserAuthWithGoogleFalse() {
        // admin has provider LOCAL
        User admin = userService.readByEmail("admin@mail.co");
        assertFalse(authUserService.isUserAuthWithGoogle(admin.getId()));
    }

    @Test
    public void testIsUserAdminOrSameTrueAdmin() {
        String email = "admin@mail.co";
        assertTrue(authUserService.isUserAdminOrSame(0, email));
    }

    @Test
    public void testIsUserAdminOrSameTrueUser() {
        String email = "user@mail.co";
        User user = userService.readByEmail(email);
        assertTrue(authUserService.isUserAdminOrSame(user.getId(), email));
    }

    @Test
    public void testIsUserAdminOrSameFalseUser() {
        String email = "user@mail.co";
        assertFalse(authUserService.isUserAdminOrSame(0, email));
    }
}
