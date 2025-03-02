package com.leandrosps.infra;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.leandrosps.application.User;
import com.leandrosps.exceptions.NotFoundException;
import lombok.Getter;

public class UserDAOInMemory implements UserDAO {
    @Getter
    private List<User> storage = new ArrayList<>(List.of(
            new User(UUID.randomUUID(), "John", "Doe", "john.doe@example.com", "senha123", 11),
            new User(UUID.randomUUID(), "Jane", "Smith", "jane.smith@example.com", "senha123", 11),
            new User(UUID.randomUUID(), "Alice", "Brown", "alice.brown@example.com", "senha123", 11)));

    /* Mutations */
    @Override
    public void persiste(User user) {
        this.storage.add(user);
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    /* Querys */
    @Override
    public Optional<User> getUserByEmail(String email) {
        return this.storage.stream().filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public User getUser(String id) {
        var userUp = this.storage.stream().filter(user -> user.getId().toString().equals(id))
        .findFirst();
        if (!userUp.isPresent()) {
            throw new NotFoundException("User Not Found!");
        }
        return userUp.get();
    }

    @Override
    public List<User> list() {
        return this.storage;
    }

}
