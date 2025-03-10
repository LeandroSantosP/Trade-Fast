package com.leandrosps.infra.database;

import java.util.List;
import java.util.Optional;

import com.leandrosps.domain.User;

public interface UserDAO {
    User getUser(String id);
    Optional<User> getUserByEmail(String id);
    void persiste(User user);
    void delete(String id);
    List<User> list();
    void clear();
}