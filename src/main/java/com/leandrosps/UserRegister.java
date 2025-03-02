package com.leandrosps;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.leandrosps.dtos.UserRegisterInput;

public class UserRegister {

    private List<User> storage = new ArrayList<>(List.of(
            new User(UUID.randomUUID(), "John", "Doe", "john.doe@example.com", "senha123"),
            new User(UUID.randomUUID(), "Jane", "Smith", "jane.smith@example.com", "senha123"),
            new User(UUID.randomUUID(), "Alice", "Brown", "alice.brown@example.com", "senha123")));

    public void excute(UserRegisterInput input) {
        Optional<User> userOp = this.storage.stream().filter(user -> user.getEmail().equalsIgnoreCase(input.email()))
                .findFirst();
        if (userOp.isPresent()) {
            throw new IllegalArgumentException("Invalid Credentials!");
        }
        this.storage.add(User.create(input.fristName(), input.lastName(), input.email(), input.password()));
    }

}
