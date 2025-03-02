package com.leandrosps;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    User getUser(String id);
    Optional<User> getUserByEmail(String id);
    void persiste(User user);
    void delete(String id);
    List<User> list();
}