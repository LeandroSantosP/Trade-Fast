package com.leandrosps;

import java.util.Optional;

import com.leandrosps.dtos.UserRegisterInput;
import com.leandrosps.exceptions.UserUnauthorizedException;

public class UserRegister {
    
    private UserDAO userDAO;
    
    public UserRegister(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String excute(UserRegisterInput input) {
          Optional<User> userOp = this.userDAO.getStorage().stream().filter(user -> user.getEmail().equalsIgnoreCase(input.email()))
                .findFirst();
        if (userOp.isPresent()) {
            throw new UserUnauthorizedException("Invalid Credentials!");
        }
        var user = User.create(input.fristName(), input.lastName(), input.email(), input.password());
        this.userDAO.getStorage().add(user);
        return user.getId().toString();
    }

}
