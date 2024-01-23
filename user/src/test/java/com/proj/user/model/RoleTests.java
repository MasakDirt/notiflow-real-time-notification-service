package com.proj.user.model;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.stream.Stream;

import static com.proj.user.TestAdvice.getViolation;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RoleTests {

    private Role role;

    @BeforeEach
    public void setCorrectRole() {
        role = Role.of("CORRECT");
        role.setId(1);
    }

    @Test
    public void testValidRole() {
        assertEquals(0, getViolation(role).size());
    }

    @ParameterizedTest
    @MethodSource("getNameArguments")
    public void testInvalidRolesNameField(String name, String error) {
        role.setName(name);
        Set<ConstraintViolation<Role>> violationSet = getViolation(role);

        assertEquals(1, violationSet.size());
        assertEquals(error, violationSet.iterator().next().getInvalidValue());
        assertEquals("Fill in the field with the name.", violationSet.iterator().next().getMessage());
    }

    private static Stream<Arguments> getNameArguments() {
        return Stream.of(
          Arguments.of("", ""),
          Arguments.of(null, null)
        );
    }
}
