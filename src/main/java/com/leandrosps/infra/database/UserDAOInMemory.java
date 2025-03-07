package com.leandrosps.infra.database;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.leandrosps.application.Roles;
import com.leandrosps.application.User;
import com.leandrosps.exceptions.NotFoundException;
import lombok.Getter;

public class UserDAOInMemory implements UserDAO {

    private static UserDAOInMemory instance = null;

    public static UserDAOInMemory getInstance() {
        if (instance != null) {
            return instance;
        }
        return new UserDAOInMemory();
    }

    private UserDAOInMemory() {
    }

    @Getter
    private List<User> storage = new ArrayList<>(List.of(
            new User(UUID.randomUUID(), "John", "Doe", "john.doe@example.com", "senha123", 11, List.of(Roles.USER),
                    LocalDateTime.now()),
            new User(UUID.randomUUID(), "Jane", "Smith", "jane.smith@example.com", "senha123", 11, List.of(Roles.USER),
                    LocalDateTime.now()),
            new User(UUID.randomUUID(), "Alice", "Brown", "alice.brown@example.com", "senha123", 11,
                    List.of(Roles.USER), LocalDateTime.now())));

    /* Mutations */
    @Override
    public void persiste(User user) {
        this.storage.add(user);
    }

    @Override
    public void delete(String id) {
        var user = this.getUser(id);
        this.storage.remove(user);
    }

    @Override
    public void clear() {
        this.storage = Collections.emptyList();
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
