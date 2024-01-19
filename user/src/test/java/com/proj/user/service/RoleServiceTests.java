package com.proj.user.service;

import com.proj.user.exception.NullRoleNameException;
import com.proj.user.model.Role;
import com.proj.user.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
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
    public void testValidCreate() {
        int sizeBefore = roleRepository.findAll().size();

        String name = "TEST";
        Role expected = Role.of(name);
        Role actual = roleService.create(name);
        expected.setId(actual.getId());
        assertEquals(expected, actual);

        int sizeAfter = roleRepository.findAll().size();
        assertTrue(sizeBefore < sizeAfter);
    }

    @ParameterizedTest
    @MethodSource("getNameArguments")
    public void testInvalidCreate(String name) {
        assertThrows(NullRoleNameException.class, () -> roleService.create(name));
    }

    private static Stream<String> getNameArguments() {
        return Stream.of("  ", null);
    }

    @Test
    public void testValidFindRoleByName() {
        String name = "CREATED";
        Role expected = roleService.create(name);
        Role actual = roleService.readByName(name);

        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidReadRoleByName() {
        String name = "INVALID_ROLE";
        assertThrows(EntityNotFoundException.class, () -> roleService.readByName(name));
    }
}
