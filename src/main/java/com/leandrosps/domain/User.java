package com.leandrosps.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Integer salt;
    private List<Roles> roles;
    private LocalDateTime createdAt = LocalDateTime.now();

    public static User create(String fristName, String lastName, String email, String password) {
        var salt = 11;
        var passwordEncrypt = BCrypt.withDefaults().hashToString(salt, password.toCharArray());
        var defaultRole = List.of(Roles.USER);
        return new User(UUID.randomUUID(), fristName, lastName, email, passwordEncrypt, salt, defaultRole,
                LocalDateTime.now());
    }

    public boolean isPasswordValid(String rawPassword) {
        return BCrypt.verifyer().verify(rawPassword.toCharArray(), this.getPassword()).verified;
    }

    public List<String> getRolesAsListString() {
        return this.getRoles().stream().map(Roles::name).toList();
    }

    public String getMajorRole() {

        String result = null;

        for (Roles role : this.roles) {
            if (role.name().equalsIgnoreCase("user")) {
                result = role.name();
            }
            if (role.name().equalsIgnoreCase("admin")) {
                result = role.name();
            }
        }

        if (result == null) {
            throw new RuntimeException("Role can not be null!");
        }

        return result;

    }

}
