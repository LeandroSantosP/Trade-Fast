package com.leandrosps;

import java.util.List;
import com.leandrosps.dtos.GetUserOutput;
import com.leandrosps.exceptions.NotFoundException;

public class UserService {
    
    private UserDAO userDAO;
    
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public GetUserOutput getUser(String id) {
        var user = this.userDAO.getStorage().stream().filter(userOp -> userOp.getId().toString().equals(id))
                .findFirst().orElseThrow(() -> new NotFoundException("User Not Exists!"));
        return new GetUserOutput(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public void deletUser(String id) {
        var user = this.userDAO.getStorage().stream().filter(userOp -> userOp.getId().toString().equals(id))
                .findFirst().orElseThrow(() -> new NotFoundException("User Not Exists!"));
        userDAO.getStorage().remove(userDAO.getStorage().indexOf(user));
    }

    public List<GetUserOutput> list() {
        return this.userDAO.getStorage().stream()
                .map(user -> new GetUserOutput(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()))
                .toList();
    }

}
