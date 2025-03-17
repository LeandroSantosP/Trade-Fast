package com.leandrosps.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    @DisplayName("Should return USER when user has only USER role")
    void shouldReturnUserRoleWhenOnlyUserRole() {
        User user = new User(
            UUID.randomUUID(),
            "John",
            "Doe",
            "john@example.com",
            "password123",
            11,
            List.of(Roles.USER),
            LocalDateTime.now()
        );

        assertEquals("USER", user.getMajorRole());
    }

    @Test
    @DisplayName("Should return ADMIN when user has only ADMIN role")
    void shouldReturnAdminRoleWhenOnlyAdminRole() {
        User user = new User(
            UUID.randomUUID(),
            "John",
            "Doe",
            "john@example.com",
            "password123",
            11,
            List.of(Roles.ADMIN),
            LocalDateTime.now()
        );

        assertEquals("ADMIN", user.getMajorRole());
    }

    @Test
    @DisplayName("Should return ADMIN when user has both ADMIN and USER roles")
    void shouldReturnAdminRoleWhenBothRoles() {
        User user = new User(
            UUID.randomUUID(),
            "John",
            "Doe",
            "john@example.com",
            "password123",
            11,
            Arrays.asList(Roles.USER, Roles.ADMIN),
            LocalDateTime.now()
        );

        assertEquals("ADMIN", user.getMajorRole());
    }

    @Test
    @DisplayName("Should throw RuntimeException when user has no valid roles")
    void shouldThrowExceptionWhenNoValidRoles() {
        User user = new User(
            UUID.randomUUID(),
            "John",
            "Doe",
            "john@example.com",
            "password123",
            11,
            List.of(),
            LocalDateTime.now()
        );

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> user.getMajorRole()
        );

        assertEquals("Role can not be null!", exception.getMessage());
    }
}
