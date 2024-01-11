package com.proj.user.service;

import com.proj.user.model.CustomOAuth2User;
import com.proj.user.model.NotificationType;
import com.proj.user.model.Provider;
import com.proj.user.model.User;
import com.proj.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public User create(User user, String roleName) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleService.readByName(roleName));
        if (!isUserHasGoogleProvider(user)) {
            user.setProvider(Provider.LOCAL);
        }

        return userRepository.saveAndFlush(user);
    }

    private boolean isUserHasGoogleProvider(User user) {
        return Objects.nonNull(user.getProvider()) &&
                user.getProvider().equals(Provider.GOOGLE);
    }

    public User processOAuthPostLogin(CustomOAuth2User customOAuth2User) {
        Optional<User> existUser = userRepository.findByEmail(customOAuth2User.getName());

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
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }

    public User readByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
