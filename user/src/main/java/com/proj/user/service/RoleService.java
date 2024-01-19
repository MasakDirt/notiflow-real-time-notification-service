package com.proj.user.service;

import com.proj.user.exception.NullRoleNameException;
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

    public Role create(String name) {
        checkValidRoleName(name);
        Role created = roleRepository.save(Role.of(name));
        log.info("Role with name {} successfully created", name);
        return created;
    }

    private void checkValidRoleName(String name) {
        if (name == null || name.trim().isEmpty()) {
            log.error("This role name is empty or null - {}", name);
            throw new NullRoleNameException("The name of Role cannot be empty!");
        }
    }

    public Role readByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role not found!"));
        log.info("Read role with name {}", name);
        return role;
    }
}
