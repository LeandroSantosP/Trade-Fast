package com.leandrosps.application;

import java.util.List;

import com.leandrosps.dtos.GetUserOutput;
import com.leandrosps.infra.UserDAO;

public class UserService {
    
    private UserDAO userDAO;
    
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public GetUserOutput getUser(String id_from, String id_to) {
        var user = this.userDAO.getUser(id_from);

        if (!user.getId().toString().equals(id_to)) {
            throw new RuntimeException("User has no authorized to this operation!");
        }
        
        return new GetUserOutput(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public void deletUser(String id) {
        var user = this.userDAO.getUser(id);
        userDAO.delete(user.getId().toString());
    }

    public List<GetUserOutput> list() {
        return this.userDAO.list().stream()
                .map(user -> new GetUserOutput(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()))
                .toList();
    }

}
