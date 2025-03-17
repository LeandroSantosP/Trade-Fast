package com.leandrosps.application.auth;

import java.util.List;
import java.util.UUID;

import com.leandrosps.dtos.GetUserOutput;
import com.leandrosps.infra.database.Repositories;
import com.leandrosps.infra.database.UserDAO;

public class UserService {
    
    private UserDAO userDAO;
    
    public UserService(Repositories uow) {
        this.userDAO = (UserDAO) uow.userRepository(); 
    }

    public GetUserOutput getUser(String id_from, String id_to) {
        var user = this.userDAO.getById(UUID.fromString(id_from));

        if (!user.getId().toString().equals(id_to)) {
            throw new RuntimeException("User has no authorized to this operation!");
        }
        
        return new GetUserOutput(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public void deleteUser(String id) {
        var user = this.userDAO.getById(UUID.fromString(id));
        userDAO.delete(user.getId());
    }

    public List<GetUserOutput> list() {
        return this.userDAO.list().stream()
                .map(user -> new GetUserOutput(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()))
                .toList();
    }

}
