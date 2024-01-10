package com.proj.user.service;

import com.proj.user.model.User;
import com.proj.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public User create(User user, String roleName) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleService.readByName(roleName));

        return userRepository.saveAndFlush(user);
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
