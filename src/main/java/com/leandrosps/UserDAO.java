package com.leandrosps;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

public class UserDAO {
    @Getter
    private List<User> storage = new ArrayList<>(List.of(
            new User(UUID.randomUUID(), "John", "Doe", "john.doe@example.com", "senha123", 11),
            new User(UUID.randomUUID(), "Jane", "Smith", "jane.smith@example.com", "senha123", 11),
            new User(UUID.randomUUID(), "Alice", "Brown", "alice.brown@example.com", "senha123", 11)));

}
