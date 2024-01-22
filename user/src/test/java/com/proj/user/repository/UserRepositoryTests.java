package com.proj.user.repository;

import com.proj.user.model.NotificationType;
import com.proj.user.model.Provider;
import com.proj.user.model.User;
import com.proj.user.service.RoleService;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class UserRepositoryTests {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserRepositoryTests(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Test
    public void testInjectedComponents() {
        AssertionsForClassTypes.assertThat(userRepository).isNotNull();
        AssertionsForClassTypes.assertThat(roleService).isNotNull();
    }

    @Test
    public void testValidFindUserByEmail() {
        String email = "correct@mail.co";
        User expected = userRepository.save(createUser(email));
        User actual = userRepository.findByEmail(email).orElseThrow();
        assertEquals(expected, actual);
    }

    private User createUser(String email) {
        User user = new User();
        user.setFullName("Correct User");
        user.setEmail(email);
        user.setNotificationType(NotificationType.EMAIL);
        user.setTelegram("@correct");
        user.setAge(23);
        user.setPassword("1234");
        user.setProvider(Provider.GOOGLE);
        user.setRole(roleService.createWithValidName("ADMIN"));

        return user;
    }

    @Test
    public void testInvalidFindUserByEmail() {
        String email = "invalid@mail.co";
        Optional<User> actual = userRepository.findByEmail(email);
        assertTrue(actual.isEmpty());
    }
}
