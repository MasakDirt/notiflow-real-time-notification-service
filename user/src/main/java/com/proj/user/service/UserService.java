package com.proj.user.service;

import com.proj.user.model.CustomOAuth2User;
import com.proj.user.model.NotificationType;
import com.proj.user.model.Provider;
import com.proj.user.model.User;
import com.proj.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public User create(User user, String roleName) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("users password is encoded");
        user.setRole(roleService.readByName(roleName));
        setUsersProvider(user);
        log.info("creation user - {} with provider - {}", user.getEmail(), user.getProvider());
        User saved = userRepository.saveAndFlush(user);
        log.info("User with email {} successfully created", user.getEmail());
        return saved;
    }

    private void setUsersProvider(User user) {
        if (!isUserHasGoogleProvider(user)) {
            user.setProvider(Provider.LOCAL);
        }
    }

    private boolean isUserHasGoogleProvider(User user) {
        return Objects.nonNull(user.getProvider()) &&
                user.getProvider().equals(Provider.GOOGLE);
    }

    public User processOAuthPostLogin(CustomOAuth2User customOAuth2User) {
        Optional<User> existUser = userRepository.findByEmail(customOAuth2User.getName());
        log.info("Login with OAuth2 - {}", customOAuth2User.getName());
        return existUser.orElseGet(() -> createNewUserFromOAuth2(customOAuth2User));
    }

    private User createNewUserFromOAuth2(CustomOAuth2User customOAuth2User) {
        User newUser = new User();
        newUser.setEmail(customOAuth2User.getName());
        newUser.setFullName(customOAuth2User.getFullName());
        newUser.setPassword(customOAuth2User.getAttributes().get("sub").toString());
        newUser.setProvider(Provider.GOOGLE);
        newUser.setNotificationType(NotificationType.EMAIL);

        return create(newUser, "USER");
    }

    public User readById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        log.info("Find user with id {}", id);
        return user;
    }

    public User readByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        log.info("Find user with email - {}", email);
        return user;
    }

    public User update(User updated) {
        User oldUser = readById(updated.getId());
        updated.setProvider(oldUser.getProvider());
        updated.setRole(oldUser.getRole());
        updated.setPassword(oldUser.getPassword());
        userRepository.saveAndFlush(updated);
        log.info("Updated user with email {}", updated.getEmail());

        return updated;
    }

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public void checkPasswords(String rawPass, String encodedPass) {
        if (!passwordEncoder.matches(rawPass, encodedPass)) {
            log.error("Passwords does not matches");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong old password!");
        }
    }
}
