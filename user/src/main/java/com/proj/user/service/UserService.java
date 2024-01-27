package com.proj.user.service;

import com.proj.user.dto.AddDataRequest;
import com.proj.user.dto.NotificationData;
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
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    public User createNewUserFromOAuth2(CustomOAuth2User customOAuth2User) {
        User newUser = new User();
        newUser.setEmail(customOAuth2User.getName());
        newUser.setFullName(customOAuth2User.getFullName());
        newUser.setPassword(customOAuth2User.getAttributes().get("sub").toString());
        newUser.setProvider(Provider.GOOGLE);
        newUser.setNotificationType(NotificationType.EMAIL);
        newUser.setTelegram("@yourtelegram");

        return saveWithSettingFields(newUser, "USER");
    }

    public User saveWithSettingFields(User user, String roleName) {
        setImportantUsersFields(user, roleName);
        userRepository.saveAndFlush(user);
        log.info("User with email {} successfully created", user.getEmail());
        return user;
    }

    private void setImportantUsersFields(User user, String roleName) {
        setUsersPassword(user);
        setUsersRole(user, roleName);
        setUsersProvider(user);
        log.info("creation user - {} with provider - {}", user.getEmail(), user.getProvider());
    }

    private void setUsersPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("users password is encoded");
    }

    private void setUsersRole(User user, String roleName) {
        user.setRole(roleService.readByName(roleName));
        log.info("users role set to {}", roleName);
    }

    private void setUsersProvider(User user) {
        if (!hasUserGoogleProvider(user)) {
            user.setProvider(Provider.LOCAL);
        }
    }

    private boolean hasUserGoogleProvider(User user) {
        return Objects.nonNull(user.getProvider()) &&
                user.getProvider().equals(Provider.GOOGLE);
    }

    public boolean isUserExist(String email) {
        return userRepository.findByEmail(email).isPresent();
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
        setFieldsToUpdatedUser(updated);
        userRepository.saveAndFlush(updated);
        log.info("Updated user with email {}", updated.getEmail());

        return updated;
    }

    private void setFieldsToUpdatedUser(User updatedUser) {
        User oldUser = readById(updatedUser.getId());
        updatedUser.setEmail(oldUser.getEmail());
        updatedUser.setProvider(oldUser.getProvider());
        updatedUser.setRole(oldUser.getRole());
        updatedUser.setPassword(oldUser.getPassword());
    }

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public void checkInvalidPasswords(String rawPass, String encodedPass) {
        if (!passwordEncoder.matches(rawPass, encodedPass)) {
            log.error("Passwords does not matches");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong old password!");
        }
    }

    public User addDataToOAuth2User(long id, AddDataRequest addDataRequest) {
        User userToUpdate = setNewFieldsToOAuth2(id, addDataRequest);
        return userRepository.saveAndFlush(userToUpdate);
    }

    private User setNewFieldsToOAuth2(long id, AddDataRequest addDataRequest) {
        User userToUpdate = readById(id);
        userToUpdate.setAge(addDataRequest.getAge());
        userToUpdate.setTelegram(addDataRequest.getTelegram());
        userToUpdate.setNotificationType(
                NotificationType.getTypeFromName(addDataRequest.getNotificationType()));
        return userToUpdate;
    }

    public void delete(User user) {
        userRepository.delete(user);
        log.info("user with email {} successfully deleted", user.getEmail());
    }

    public void sendDataToTelegramNotification(String recipientEmail, long senderId) {
        User recipient = readByEmail(recipientEmail);
        User sender = readById(senderId);

        String recipientUserTelegram = recipient.getTelegram();
        String senderUserName = sender.getFullName();
        log.info("{} want to receive a message from {}", recipientEmail, sender.getEmail());
        String message = String.format("Hello %s, I like that you want to speak with me, that`s crazy)))", recipient.getFullName());
        restTemplate.postForLocation("http://TELEGRAM/api/v1/telegram/send",
                new NotificationData(recipientUserTelegram, senderUserName, message));

    }
}
