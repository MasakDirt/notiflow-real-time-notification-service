package com.proj.user.service;

import com.proj.user.exception.RoleNameException;
import com.proj.user.model.Role;
import com.proj.user.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class RoleServiceTests {

    private final RoleService roleService;
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceTests(RoleService roleService, RoleRepository roleRepository) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    @Test
    public void testInjectedComponents() {
        assertThat(roleService).isNotNull();
        assertThat(roleRepository).isNotNull();
    }

    @Test
    public void testValidCreateWithValidName() {
        int sizeBeforeCreation = roleRepository.findAll().size();

        String name = "TEST";
        Role expected = Role.of(name);
        Role actual = roleService.createWithValidName(name);
        expected.setId(actual.getId());
        assertEquals(expected, actual);

        int sizeAfterCreation = roleRepository.findAll().size();
        assertTrue(sizeBeforeCreation < sizeAfterCreation);
    }

    @ParameterizedTest
    @MethodSource("getInvalidNameArguments")
    public void testInvalidCreateWithValidName(String name) {
        assertThrows(RoleNameException.class, () -> roleService.createWithValidName(name));
    }

    private static Stream<String> getInvalidNameArguments() {
        return Stream.of("  ", null);
    }

    @Test
    public void testValidFindRoleByName() {
        String name = "CREATED";
        Role expected = roleService.createWithValidName(name);
        Role actual = roleService.readByName(name);

        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidReadRoleByName() {
        String name = "INVALID_ROLE";
        assertThrows(EntityNotFoundException.class, () -> roleService.readByName(name));
    }
}
