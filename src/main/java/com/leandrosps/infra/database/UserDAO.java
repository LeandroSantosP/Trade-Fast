package com.leandrosps.infra.database;

import java.util.Optional;
import java.util.UUID;

import com.leandrosps.domain.User;

public interface UserDAO extends Repository<User, UUID> {
    Optional<User> getUserByEmail(String id);
    void clear();
}