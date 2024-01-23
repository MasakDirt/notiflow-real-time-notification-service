package com.proj.user.service;

import com.proj.user.exception.RoleNameException;
import com.proj.user.model.Role;
import com.proj.user.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role createWithValidName(String name) {
        checkValidRoleName(name);
        Role created = roleRepository.save(Role.of(name));
        log.info("Role with name {} successfully created", name);
        return created;
    }

    private void checkValidRoleName(String name) {
        if (isInvalidRoleName(name)) {
            log.error("This role name is invalid - {}", name == null ? null : "empty");
            throw new RoleNameException("The name of Role cannot be empty!");
        }
    }

    private boolean isInvalidRoleName(String name) {
        return name == null || name.trim().isEmpty();
    }

    public Role readByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role not found!"));
        log.info("Read role with name {}", name);
        return role;
    }
}
