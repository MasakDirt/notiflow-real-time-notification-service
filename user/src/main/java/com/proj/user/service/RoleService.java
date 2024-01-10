package com.proj.user.service;

import com.proj.user.model.Role;
import com.proj.user.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role create(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullPointerException("The name of Role cannot be empty!");
        }
        return roleRepository.save(Role.of(name));
    }

    public Role readByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role not found!"));
    }
}
