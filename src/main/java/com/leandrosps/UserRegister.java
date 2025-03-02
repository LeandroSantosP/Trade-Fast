package com.leandrosps;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.leandrosps.dtos.GetUserOutput;
import com.leandrosps.dtos.UserRegisterInput;
import com.leandrosps.exceptions.NotFoundException;
import com.leandrosps.exceptions.UserUnauthorizedException;

public class UserRegister {

    private List<User> storage = new ArrayList<>(List.of(
            new User(UUID.randomUUID(), "John", "Doe", "john.doe@example.com", "senha123", 11),
            new User(UUID.randomUUID(), "Jane", "Smith", "jane.smith@example.com", "senha123", 11),
            new User(UUID.randomUUID(), "Alice", "Brown", "alice.brown@example.com", "senha123", 11)));

    public UUID register(UserRegisterInput input) {
        Optional<User> userOp = this.storage.stream().filter(user -> user.getEmail().equalsIgnoreCase(input.email()))
                .findFirst();
        if (userOp.isPresent()) {
            throw new UserUnauthorizedException("Invalid Credentials!");
        }
        var user = User.create(input.fristName(), input.lastName(), input.email(), input.password());
        this.storage.add(user);
        return user.getId();
    }

    public GetUserOutput getUser(String id) {
        var user = this.storage.stream().filter(userOp -> userOp.getId().toString().equals(id))
                .findFirst().orElseThrow(() -> new NotFoundException("User Not Exists!"));
        return new GetUserOutput(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public void deletUser(String id) {
        var user = this.storage.stream().filter(userOp -> userOp.getId().toString().equals(id))
                .findFirst().orElseThrow(() -> new NotFoundException("User Not Exists!"));
        storage.remove(storage.indexOf(user));
    }

    public List<GetUserOutput> list() {
        return this.storage.stream()
                .map(user -> new GetUserOutput(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()))
                .toList();
    }

}
