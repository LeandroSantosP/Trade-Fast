package com.leandrosps;

import java.util.UUID;
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

    public static User create(String fristName, String lastName, String email, String password) {
        return new User(UUID.randomUUID(), fristName, lastName, email, password);
    }
}
