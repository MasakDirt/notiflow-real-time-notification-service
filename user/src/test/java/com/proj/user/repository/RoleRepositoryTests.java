package com.proj.user.repository;

import com.proj.user.model.Role;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class RoleRepositoryTests {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleRepositoryTests(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Test
    public void testInjectedComponents() {
        AssertionsForClassTypes.assertThat(roleRepository).isNotNull();
    }

    @Test
    public void testValidFindRoleByName() {
        String name = "CREATED";
        Role expected = roleRepository.save(createRole(name));
        Role actual = roleRepository.findByName(name).orElseThrow();
        assertEquals(expected, actual);
    }

    private Role createRole(String name) {
        Role role = new Role();
        role.setName(name);

        return role;
    }

    @Test
    public void testInvalidFindRoleByName() {
        String name = "INVALID_ROLE";
        Optional<Role> actual = roleRepository.findByName(name);
        assertTrue(actual.isEmpty());
    }
}
