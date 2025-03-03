package com.leandrosps.application;

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

    public static User create(String fristName, String lastName, String email, String password) {
        var salt = 11;
        var passwordEncrypt = BCrypt.withDefaults().hashToString(salt, password.toCharArray());
        return new User(UUID.randomUUID(), fristName, lastName, email, passwordEncrypt, salt);
    }

    public boolean isPasswordValid(String rawPassword) {
        return BCrypt.verifyer().verify(rawPassword.toCharArray(), this.getPassword().toCharArray()).verified;
    }
}
