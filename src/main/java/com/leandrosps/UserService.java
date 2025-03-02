package com.leandrosps;

import java.util.List;
import com.leandrosps.dtos.GetUserOutput;

public class UserService {
    
    private UserDAO userDAO;
    
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public GetUserOutput getUser(String id) {
        var user = this.userDAO.getUser(id);
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
